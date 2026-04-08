package com.chat.controller;

import com.chat.dto.ApiResponse;
import com.chat.dto.ChatMessageDTO;
import com.chat.dto.ContactDTO;
import com.chat.dto.MatchRequest;
import com.chat.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天相关接口控制器
 * 处理消息、匹配、通讯录等 RESTful 请求
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * 发送消息
     * POST /api/messages
     */
    @PostMapping("/messages")
    public ApiResponse<ChatMessageDTO> sendMessage(
            @RequestParam Long sendId,
            @RequestParam Long receiveId,
            @RequestParam String content) {
        try {
            ChatMessageDTO message = chatService.sendMessage(sendId, receiveId, content);
            return ApiResponse.success(message, "消息发送成功");
        } catch (Exception e) {
            log.error("发送消息失败", e);
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 获取聊天记录
     * GET /api/messages?sendId=xxx&receiveId=xxx&page=0&size=20
     */
    @GetMapping("/messages")
    public ApiResponse<List<ChatMessageDTO>> getMessages(
            @RequestParam Long sendId,
            @RequestParam Long receiveId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            List<ChatMessageDTO> messages = chatService.getMessages(sendId, receiveId, page, size);
            return ApiResponse.success(messages);
        } catch (Exception e) {
            log.error("获取聊天记录失败", e);
            return ApiResponse.error(500, "获取聊天记录失败");
        }
    }

    /**
     * 标记消息为已读
     * POST /api/messages/read
     */
    @PostMapping("/messages/read")
    public ApiResponse<Void> markMessagesAsRead(
            @RequestParam Long receiveId,
            @RequestParam Long sendId) {
        try {
            chatService.markMessagesAsRead(receiveId, sendId);
            return ApiResponse.success(null, "操作成功");
        } catch (Exception e) {
            log.error("标记已读失败", e);
            return ApiResponse.error(500, "操作失败");
        }
    }

    /**
     * 获取未读消息数量
     * GET /api/messages/unread?accountId=xxx
     */
    @GetMapping("/messages/unread")
    public ApiResponse<Map<String, Object>> getUnreadCount(@RequestParam Long accountId) {
        try {
            long count = chatService.getUnreadCount(accountId);
            Map<String, Object> data = new HashMap<>();
            data.put("unreadCount", count);
            return ApiResponse.success(data);
        } catch (Exception e) {
            log.error("获取未读消息数失败", e);
            return ApiResponse.error(500, "获取未读消息数失败");
        }
    }

    /**
     * 清空聊天记录
     * DELETE /api/messages/clear?sendId=xxx&receiveId=xxx
     */
    @DeleteMapping("/messages/clear")
    public ApiResponse<Void> clearMessages(
            @RequestParam Long sendId,
            @RequestParam Long receiveId) {
        try {
            chatService.clearMessages(sendId, receiveId);
            return ApiResponse.success(null, "聊天记录已清空");
        } catch (Exception e) {
            log.error("清空聊天记录失败", e);
            return ApiResponse.error(500, "操作失败");
        }
    }

    /**
     * 发起匹配
     * POST /api/match/start
     */
    @PostMapping("/match/start")
    public ApiResponse<Map<String, Object>> startMatching(@Valid @RequestBody MatchRequest request) {
        try {
            var match = chatService.startMatching(
                request.getAccountId(),
                request.getGenderPreference(),
                request.getMinAge(),
                request.getMaxAge()
            );
            
            Map<String, Object> data = new HashMap<>();
            data.put("matchId", match.getId());
            data.put("status", "matching");
            data.put("message", "开始匹配，请稍候...");
            
            return ApiResponse.success(data, "匹配已开始");
        } catch (Exception e) {
            log.error("发起匹配失败", e);
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 取消匹配
     * POST /api/match/cancel?accountId=xxx
     */
    @PostMapping("/match/cancel")
    public ApiResponse<Void> cancelMatching(@RequestParam Long accountId) {
        try {
            chatService.cancelMatching(accountId);
            return ApiResponse.success(null, "匹配已取消");
        } catch (Exception e) {
            log.error("取消匹配失败", e);
            return ApiResponse.error(500, "操作失败");
        }
    }

    /**
     * 获取通讯录列表
     * GET /api/contacts?accountId=xxx
     */
    @GetMapping("/contacts")
    public ApiResponse<List<ContactDTO>> getContacts(@RequestParam Long accountId) {
        try {
            List<ContactDTO> contacts = chatService.getContacts(accountId);
            return ApiResponse.success(contacts);
        } catch (Exception e) {
            log.error("获取通讯录失败", e);
            return ApiResponse.error(500, "获取通讯录失败");
        }
    }

    /**
     * 删除好友
     * DELETE /api/contacts?accountId=xxx&friendId=xxx
     */
    @DeleteMapping("/contacts")
    public ApiResponse<Void> deleteFriend(
            @RequestParam Long accountId,
            @RequestParam Long friendId) {
        try {
            chatService.deleteFriend(accountId, friendId);
            return ApiResponse.success(null, "好友已删除");
        } catch (Exception e) {
            log.error("删除好友失败", e);
            return ApiResponse.error(500, "操作失败");
        }
    }

    /**
     * 拉黑好友
     * POST /api/contacts/black?accountId=xxx&friendId=xxx
     */
    @PostMapping("/contacts/black")
    public ApiResponse<Void> blacklistFriend(
            @RequestParam Long accountId,
            @RequestParam Long friendId) {
        try {
            chatService.blacklistFriend(accountId, friendId);
            return ApiResponse.success(null, "已拉黑该用户");
        } catch (Exception e) {
            log.error("拉黑好友失败", e);
            return ApiResponse.error(500, "操作失败");
        }
    }

    /**
     * 解除拉黑
     * POST /api/contacts/unblack?accountId=xxx&friendId=xxx
     */
    @PostMapping("/contacts/unblack")
    public ApiResponse<Void> unblacklistFriend(
            @RequestParam Long accountId,
            @RequestParam Long friendId) {
        try {
            chatService.unblacklistFriend(accountId, friendId);
            return ApiResponse.success(null, "已解除拉黑");
        } catch (Exception e) {
            log.error("解除拉黑失败", e);
            return ApiResponse.error(500, "操作失败");
        }
    }
}
