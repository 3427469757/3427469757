package com.chat.service;

import com.chat.dto.ChatMessageDTO;
import com.chat.dto.ContactDTO;
import com.chat.entity.*;
import com.chat.repository.*;
import com.chat.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 聊天核心业务服务
 * 处理消息、匹配、通讯录等业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository messageRepository;
    private final ChatMatchRepository matchRepository;
    private final ChatContactRepository contactRepository;
    private final UserAccountRepository accountRepository;
    private final AnonymousClientRepository clientRepository;

    /**
     * 发送消息
     */
    @Transactional
    public ChatMessageDTO sendMessage(Long sendId, Long receiveId, String content) {
        // 校验好友关系
        if (!contactRepository.areFriends(sendId, receiveId)) {
            throw new RuntimeException("非好友无法发送消息");
        }
        
        // 校验是否被拉黑
        if (contactRepository.isBlacklisted(sendId, receiveId)) {
            throw new RuntimeException("您已被对方拉黑");
        }
        
        // 消息长度校验
        if (content == null || content.length() > 100) {
            throw new RuntimeException("消息长度不能超过 100 字");
        }
        
        // 敏感词过滤（简化版）
        if (containsSensitiveWords(content)) {
            throw new RuntimeException("消息包含敏感词");
        }
        
        ChatMessage message = new ChatMessage();
        message.setId(IdGenerator.generateSnowflakeId());
        message.setSendId(sendId);
        message.setReceiveId(receiveId);
        message.setContent(content);
        message.setType("text");
        message.setIsRead(0);
        
        messageRepository.save(message);
        log.info("消息发送成功：{} -> {}", sendId, receiveId);
        
        return ChatMessageDTO.fromEntity(message);
    }

    /**
     * 获取聊天记录（分页）
     */
    public List<ChatMessageDTO> getMessages(Long id1, Long id2, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<ChatMessage> messagePage = messageRepository.findBidirectionalMessages(id1, id2, pageRequest);
        
        return messagePage.getContent().stream()
                .map(ChatMessageDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 标记消息为已读
     */
    @Transactional
    public void markMessagesAsRead(Long receiveId, Long sendId) {
        messageRepository.markAsRead(receiveId, sendId);
        log.info("消息已标记为已读：{} <- {}", receiveId, sendId);
    }

    /**
     * 获取未读消息数量
     */
    public long getUnreadCount(Long accountId) {
        return messageRepository.countByReceiveIdAndIsRead(accountId, 0);
    }

    /**
     * 清空聊天记录
     */
    @Transactional
    public void clearMessages(Long id1, Long id2) {
        messageRepository.deleteBidirectionalMessages(id1, id2);
        log.info("聊天记录已清空：{} <-> {}", id1, id2);
    }

    /**
     * 发起匹配
     */
    @Transactional
    public ChatMatch startMatching(Long accountId, Integer genderPreference, Integer minAge, Integer maxAge) {
        // 检查是否已有进行中的匹配
        if (matchRepository.findByAccountIdAndStatus(accountId, ChatMatch.Status.MATCHING).isPresent()) {
            throw new RuntimeException("已有进行中的匹配，请先取消");
        }
        
        ChatMatch match = new ChatMatch();
        match.setId(IdGenerator.generateSnowflakeId());
        match.setAccountId(accountId);
        match.setStatus(ChatMatch.Status.MATCHING);
        match.setGenderPreference(genderPreference);
        match.setMinAge(minAge);
        match.setMaxAge(maxAge);
        
        matchRepository.save(match);
        log.info("开始匹配：accountId={}", accountId);
        
        return match;
    }

    /**
     * 取消匹配
     */
    @Transactional
    public void cancelMatching(Long accountId) {
        matchRepository.cancelMatching(accountId);
        log.info("取消匹配：accountId={}", accountId);
    }

    /**
     * 匹配成功（内部调用）
     */
    @Transactional
    public void completeMatching(Long accountId, Long targetId, Long matchId) {
        // 更新匹配记录
        matchRepository.updateStatus(matchId, ChatMatch.Status.SUCCESS);
        
        // 添加双向好友关系
        addFriendship(accountId, targetId);
        
        log.info("匹配成功：{} <-> {}", accountId, targetId);
    }

    /**
     * 添加好友关系
     */
    private void addFriendship(Long accountId, Long friendId) {
        if (!contactRepository.areFriends(accountId, friendId)) {
            ChatContact contact1 = new ChatContact();
            contact1.setId(IdGenerator.generateSnowflakeId());
            contact1.setAccountId(accountId);
            contact1.setFriendId(friendId);
            contactRepository.save(contact1);
            
            ChatContact contact2 = new ChatContact();
            contact2.setId(IdGenerator.generateSnowflakeId());
            contact2.setAccountId(friendId);
            contact2.setFriendId(accountId);
            contactRepository.save(contact2);
        }
    }

    /**
     * 获取通讯录列表
     */
    public List<ContactDTO> getContacts(Long accountId) {
        List<ChatContact> contacts = contactRepository.findByAccountIdOrderByCreateTimeDesc(accountId);
        
        return contacts.stream().map(contact -> {
            ContactDTO dto = new ContactDTO();
            dto.setId(contact.getId());
            dto.setAccountId(contact.getAccountId());
            dto.setFriendId(contact.getFriendId());
            dto.setRemark(contact.getRemark());
            dto.setBlackStatus(contact.getBlackStatus());
            dto.setCreateTime(contact.getCreateTime());
            
            // 获取好友信息
            accountRepository.findById(contact.getFriendId()).ifPresent(friend -> {
                dto.setFriendNickname(friend.getNickname());
                dto.setFriendAvatar(friend.getAvatar());
                dto.setFriendGender(friend.getGender());
                dto.setFriendAge(friend.getAge());
                dto.setFriendRegion(friend.getRegion());
            });
            
            // 获取未读消息数
            Long unreadCount = contactRepository.countUnreadMessages(accountId, contact.getFriendId());
            dto.setUnreadCount(unreadCount != null ? unreadCount : 0L);
            
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 删除好友
     */
    @Transactional
    public void deleteFriend(Long accountId, Long friendId) {
        // 删除好友关系
        contactRepository.deleteFriendship(accountId, friendId);
        
        // 删除聊天记录
        messageRepository.deleteBidirectionalMessages(accountId, friendId);
        
        log.info("删除好友：{} - {}", accountId, friendId);
    }

    /**
     * 拉黑好友
     */
    @Transactional
    public void blacklistFriend(Long accountId, Long friendId) {
        contactRepository.updateBlackStatus(accountId, friendId, 1);
        log.info("拉黑好友：{} - {}", accountId, friendId);
    }

    /**
     * 解除拉黑
     */
    @Transactional
    public void unblacklistFriend(Long accountId, Long friendId) {
        contactRepository.updateBlackStatus(accountId, friendId, 0);
        log.info("解除拉黑：{} - {}", accountId, friendId);
    }

    /**
     * 定时清理 7 天前的消息
     * 每天凌晨 2 点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void cleanupOldMessages() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(7);
        int deletedCount = messageRepository.deleteMessagesOlderThan(threshold);
        log.info("清理过期消息完成，删除 {} 条记录", deletedCount);
    }

    /**
     * 简单的敏感词检测（实际项目应使用更完善的敏感词库）
     */
    private boolean containsSensitiveWords(String content) {
        // 简化示例，实际应加载敏感词库
        String[] sensitiveWords = {"敏感词 1", "敏感词 2"};
        for (String word : sensitiveWords) {
            if (content.contains(word)) {
                return true;
            }
        }
        return false;
    }
}
