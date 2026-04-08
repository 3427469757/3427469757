package com.anonymous.chat.controller;

import com.anonymous.chat.common.ApiResponse;
import com.anonymous.chat.dto.AccountCreateRequest;
import com.anonymous.chat.dto.AccountUpdateRequest;
import com.anonymous.chat.entity.AnonymousAccount;
import com.anonymous.chat.service.AnonymousAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 虚拟账号 Controller
 * Account Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AnonymousAccountService accountService;

    /**
     * 创建虚拟账号
     * POST /api/account/create
     */
    @PostMapping("/create")
    public ApiResponse<Map<String, Object>> createAccount(@RequestBody @Validated AccountCreateRequest request) {
        try {
            AnonymousAccount account = accountService.createAccount(request);
            
            Map<String, Object> data = new HashMap<>();
            data.put("account", account);
            
            return ApiResponse.success("账号创建成功", data);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            log.error("创建账号失败", e);
            return ApiResponse.fail("创建账号失败：" + e.getMessage());
        }
    }

    /**
     * 更新账号资料
     * POST /api/account/update
     */
    @PostMapping("/update")
    public ApiResponse<Map<String, Object>> updateAccount(@RequestBody @Validated AccountUpdateRequest request) {
        try {
            AnonymousAccount account = accountService.updateAccount(request);
            
            Map<String, Object> data = new HashMap<>();
            data.put("account", account);
            
            return ApiResponse.success("资料更新成功", data);
        } catch (IllegalArgumentException | SecurityException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            log.error("更新账号失败", e);
            return ApiResponse.fail("更新账号失败：" + e.getMessage());
        }
    }

    /**
     * 删除账号
     * POST /api/account/delete
     */
    @PostMapping("/delete")
    public ApiResponse<Void> deleteAccount(@RequestParam Long accountId, 
                                           @RequestParam String clientId) {
        try {
            accountService.deleteAccount(accountId, clientId);
            return ApiResponse.success("账号删除成功", null);
        } catch (IllegalArgumentException | SecurityException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            log.error("删除账号失败", e);
            return ApiResponse.fail("删除账号失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户所有账号
     * GET /api/account/list
     */
    @GetMapping("/list")
    public ApiResponse<List<AnonymousAccount>> getAccountList(@RequestParam String clientId) {
        try {
            List<AnonymousAccount> accounts = accountService.getAccountsByClientId(clientId);
            return ApiResponse.success(accounts);
        } catch (Exception e) {
            log.error("获取账号列表失败", e);
            return ApiResponse.fail("获取账号列表失败：" + e.getMessage());
        }
    }

    /**
     * 统计账号数量
     * GET /api/account/count
     */
    @GetMapping("/count")
    public ApiResponse<Map<String, Integer>> getAccountCount(@RequestParam String clientId) {
        try {
            int count = accountService.countAccountsByClientId(clientId);
            int maxAllowed = 3; // Constants.MAX_ACCOUNTS_PER_CLIENT
            
            Map<String, Integer> data = new HashMap<>();
            data.put("count", count);
            data.put("maxAllowed", maxAllowed);
            data.put("canCreate", count < maxAllowed ? 1 : 0);
            
            return ApiResponse.success(data);
        } catch (Exception e) {
            log.error("获取账号数量失败", e);
            return ApiResponse.fail("获取账号数量失败：" + e.getMessage());
        }
    }

    /**
     * 获取账号详情
     * GET /api/account/detail
     */
    @GetMapping("/detail")
    public ApiResponse<AnonymousAccount> getAccountDetail(@RequestParam Long accountId) {
        try {
            AnonymousAccount account = accountService.getAccountById(accountId);
            if (account == null || account.getIsDeleted() == 1) {
                return ApiResponse.error("账号不存在");
            }
            return ApiResponse.success(account);
        } catch (Exception e) {
            log.error("获取账号详情失败", e);
            return ApiResponse.fail("获取账号详情失败：" + e.getMessage());
        }
    }
}
