package com.anonymous.chat.controller;

import cn.hutool.core.util.IdUtil;
import com.anonymous.chat.common.ApiResponse;
import com.anonymous.chat.entity.AnonymousClient;
import com.anonymous.chat.service.AnonymousClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户端 Controller
 * Client Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private AnonymousClientService clientService;

    /**
     * 注册或获取客户端
     * POST /api/client/register
     */
    @PostMapping("/register")
    public ApiResponse<Map<String, Object>> registerClient(@RequestBody Map<String, String> request) {
        try {
            String clientId = request.get("clientId");
            
            if (clientId == null || clientId.trim().isEmpty()) {
                // 前端未传 clientId，返回错误
                return ApiResponse.error("clientId 不能为空");
            }

            AnonymousClient client = clientService.registerOrGetClient(clientId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("clientId", client.getClientId());
            data.put("createTime", client.getCreateTime());
            
            return ApiResponse.success("客户端注册成功", data);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            log.error("客户端注册失败", e);
            return ApiResponse.fail("客户端注册失败：" + e.getMessage());
        }
    }

    /**
     * 生成新的 clientId（供前端调用）
     * GET /api/client/generate-id
     */
    @GetMapping("/generate-id")
    public ApiResponse<Map<String, String>> generateClientId() {
        try {
            // 生成 32 位 UUID（无横杠）
            String clientId = IdUtil.fastSimpleUUID();
            
            Map<String, String> data = new HashMap<>();
            data.put("clientId", clientId);
            
            return ApiResponse.success("clientId 生成成功", data);
        } catch (Exception e) {
            log.error("生成 clientId 失败", e);
            return ApiResponse.fail("生成 clientId 失败：" + e.getMessage());
        }
    }

    /**
     * 验证 clientId 是否有效
     * GET /api/client/validate
     */
    @GetMapping("/validate")
    public ApiResponse<Map<String, Boolean>> validateClientId(@RequestParam String clientId) {
        try {
            boolean isValid = false;
            
            if (clientId != null && clientId.length() == 32) {
                AnonymousClient client = clientService.getClientByClientId(clientId);
                isValid = (client != null);
            }
            
            Map<String, Boolean> data = new HashMap<>();
            data.put("valid", isValid);
            
            return ApiResponse.success(data);
        } catch (Exception e) {
            log.error("验证 clientId 失败", e);
            return ApiResponse.fail("验证 clientId 失败：" + e.getMessage());
        }
    }
}
