package com.anonymous.chat.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.anonymous.chat.common.Constants;
import com.anonymous.chat.dto.AccountCreateRequest;
import com.anonymous.chat.dto.AccountUpdateRequest;
import com.anonymous.chat.entity.AnonymousAccount;
import com.anonymous.chat.mapper.AnonymousAccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 虚拟账号 Service 实现类
 * Account Service Implementation
 */
@Slf4j
@Service
public class AnonymousAccountService {

    @Autowired
    private AnonymousAccountMapper accountMapper;

    /**
     * 创建虚拟账号
     * @param request 创建请求
     * @return 创建的账号
     */
    @Transactional(rollbackFor = Exception.class)
    public AnonymousAccount createAccount(AccountCreateRequest request) {
        // 校验 clientId
        if (StrUtil.isBlank(request.getClientId())) {
            throw new IllegalArgumentException("clientId 不能为空");
        }

        // 检查账号数量上限
        int currentCount = accountMapper.countByClientId(request.getClientId());
        if (currentCount >= Constants.MAX_ACCOUNTS_PER_CLIENT) {
            throw new IllegalArgumentException("最多可创建" + Constants.MAX_ACCOUNTS_PER_CLIENT + "个账号，请删除多余账号后再试");
        }

        // 校验昵称
        if (StrUtil.isBlank(request.getNickname()) || 
            request.getNickname().length() > Constants.MAX_NICKNAME_LENGTH) {
            throw new IllegalArgumentException("昵称长度必须在 1-32 个字符之间");
        }

        // 校验年龄
        if (request.getAge() == null || 
            request.getAge() < Constants.MIN_AGE || 
            request.getAge() > Constants.MAX_AGE) {
            throw new IllegalArgumentException("年龄必须在 1-100 之间");
        }

        // 校验性别
        if (request.getGender() == null || 
            request.getGender() < 0 || 
            request.getGender() > 2) {
            throw new IllegalArgumentException("性别参数错误");
        }

        // 设置默认头像
        String avatar = request.getAvatar();
        if (StrUtil.isBlank(avatar)) {
            // 随机选择默认头像
            int randomIndex = (int) (Math.random() * 5) + 1;
            avatar = Constants.DEFAULT_AVATAR_PREFIX + "avatar_" + randomIndex + ".jpg";
        }

        // 创建账号
        AnonymousAccount account = new AnonymousAccount();
        account.setId(IdUtil.getSnowflakeNextId());
        account.setClientId(request.getClientId());
        account.setNickname(request.getNickname());
        account.setGender(request.getGender());
        account.setAge(request.getAge());
        account.setRegion(StrUtil.isNotBlank(request.getRegion()) ? request.getRegion() : "未知地区");
        account.setSignature(StrUtil.isNotBlank(request.getSignature()) ? request.getSignature() : "");
        account.setAvatar(avatar);
        account.setIsDeleted(Constants.NOT_DELETED);
        account.setCreateTime(LocalDateTime.now());
        account.setUpdateTime(LocalDateTime.now());

        accountMapper.insert(account);
        log.info("创建账号成功：accountId={}, clientId={}", account.getId(), request.getClientId());

        return account;
    }

    /**
     * 更新账号资料
     * @param request 更新请求
     * @return 更新后的账号
     */
    @Transactional(rollbackFor = Exception.class)
    public AnonymousAccount updateAccount(AccountUpdateRequest request) {
        // 校验参数
        if (request.getAccountId() == null) {
            throw new IllegalArgumentException("账号 ID 不能为空");
        }
        if (StrUtil.isBlank(request.getClientId())) {
            throw new IllegalArgumentException("clientId 不能为空");
        }

        // 校验账号归属
        int ownershipCount = accountMapper.checkAccountOwnership(request.getAccountId(), request.getClientId());
        if (ownershipCount == 0) {
            throw new SecurityException("无权操作该账号");
        }

        // 查询账号
        AnonymousAccount account = accountMapper.selectById(request.getAccountId());
        if (account == null || account.getIsDeleted() == Constants.DELETED) {
            throw new IllegalArgumentException("账号不存在或已删除");
        }

        // 更新资料（只更新非空字段）
        if (StrUtil.isNotBlank(request.getNickname())) {
            if (request.getNickname().length() > Constants.MAX_NICKNAME_LENGTH) {
                throw new IllegalArgumentException("昵称长度不能超过 32 个字符");
            }
            account.setNickname(request.getNickname());
        }
        if (request.getGender() != null) {
            if (request.getGender() < 0 || request.getGender() > 2) {
                throw new IllegalArgumentException("性别参数错误");
            }
            account.setGender(request.getGender());
        }
        if (request.getAge() != null) {
            if (request.getAge() < Constants.MIN_AGE || request.getAge() > Constants.MAX_AGE) {
                throw new IllegalArgumentException("年龄必须在 1-100 之间");
            }
            account.setAge(request.getAge());
        }
        if (request.getRegion() != null) {
            if (request.getRegion().length() > 64) {
                throw new IllegalArgumentException("地区长度不能超过 64 个字符");
            }
            account.setRegion(request.getRegion());
        }
        if (request.getSignature() != null) {
            if (request.getSignature().length() > Constants.MAX_SIGNATURE_LENGTH) {
                throw new IllegalArgumentException("个性签名长度不能超过 128 个字符");
            }
            account.setSignature(request.getSignature());
        }
        if (request.getAvatar() != null) {
            account.setAvatar(request.getAvatar());
        }

        account.setUpdateTime(LocalDateTime.now());
        accountMapper.updateById(account);

        log.info("更新账号成功：accountId={}", account.getId());
        return account;
    }

    /**
     * 删除账号（逻辑删除）
     * @param accountId 账号 ID
     * @param clientId 客户端 ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAccount(Long accountId, String clientId) {
        if (accountId == null || StrUtil.isBlank(clientId)) {
            throw new IllegalArgumentException("参数错误");
        }

        // 校验账号归属
        int ownershipCount = accountMapper.checkAccountOwnership(accountId, clientId);
        if (ownershipCount == 0) {
            throw new SecurityException("无权操作该账号");
        }

        // 执行逻辑删除
        AnonymousAccount account = accountMapper.selectById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("账号不存在");
        }

        account.setIsDeleted(Constants.DELETED);
        account.setUpdateTime(LocalDateTime.now());
        accountMapper.updateById(account);

        log.info("删除账号成功：accountId={}", accountId);
    }

    /**
     * 获取用户所有账号
     * @param clientId 客户端 ID
     * @return 账号列表
     */
    public List<AnonymousAccount> getAccountsByClientId(String clientId) {
        return accountMapper.selectByClientId(clientId);
    }

    /**
     * 统计账号数量
     * @param clientId 客户端 ID
     * @return 账号数量
     */
    public int countAccountsByClientId(String clientId) {
        return accountMapper.countByClientId(clientId);
    }

    /**
     * 根据 ID 获取账号
     */
    public AnonymousAccount getAccountById(Long accountId) {
        return accountMapper.selectById(accountId);
    }

    /**
     * 校验账号归属
     */
    public boolean checkAccountOwnership(Long accountId, String clientId) {
        return accountMapper.checkAccountOwnership(accountId, clientId) > 0;
    }
}
