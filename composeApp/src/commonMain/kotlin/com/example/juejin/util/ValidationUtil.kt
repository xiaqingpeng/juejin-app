package com.example.juejin.util

/**
 * 输入验证工具类
 */
object ValidationUtil {
    
    /**
     * 验证用户名
     * 规则：2-20个字符，支持中英文、数字、下划线
     */
    fun validateUsername(username: String): ValidationResult {
        return when {
            username.isBlank() -> ValidationResult(false, "用户名不能为空")
            username.length < 2 -> ValidationResult(false, "用户名至少2个字符")
            username.length > 20 -> ValidationResult(false, "用户名最多20个字符")
            !username.matches(Regex("^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$")) -> 
                ValidationResult(false, "用户名只能包含中英文、数字、下划线")
            else -> ValidationResult(true)
        }
    }
    
    /**
     * 验证职位
     * 规则：1-30个字符
     */
    fun validatePosition(position: String): ValidationResult {
        return when {
            position.isBlank() -> ValidationResult(true) // 职位可以为空
            position.length > 30 -> ValidationResult(false, "职位最多30个字符")
            else -> ValidationResult(true)
        }
    }
    
    /**
     * 验证公司
     * 规则：1-30个字符
     */
    fun validateCompany(company: String): ValidationResult {
        return when {
            company.isBlank() -> ValidationResult(true) // 公司可以为空
            company.length > 30 -> ValidationResult(false, "公司名称最多30个字符")
            else -> ValidationResult(true)
        }
    }
    
    /**
     * 验证简介
     * 规则：最多100个字符
     */
    fun validateBio(bio: String): ValidationResult {
        return when {
            bio.isBlank() -> ValidationResult(true) // 简介可以为空
            bio.length > 100 -> ValidationResult(false, "简介最多100个字符")
            else -> ValidationResult(true)
        }
    }
    
    /**
     * 验证博客地址
     * 规则：必须是有效的 URL 格式
     */
    fun validateBlogUrl(url: String): ValidationResult {
        if (url.isBlank()) return ValidationResult(true) // URL 可以为空
        
        return when {
            url.length > 100 -> ValidationResult(false, "URL 最多100个字符")
            !url.matches(Regex("^https?://[\\w\\-]+(\\.[\\w\\-]+)+[/#?]?.*$")) -> 
                ValidationResult(false, "请输入有效的 URL（以 http:// 或 https:// 开头）")
            else -> ValidationResult(true)
        }
    }
    
    /**
     * 验证 GitHub 用户名
     * 规则：
     * - 只能包含字母、数字和连字符
     * - 不能以连字符开头或结尾
     * - 不能包含连续的连字符
     * - 最多39个字符
     */
    fun validateGithub(github: String): ValidationResult {
        if (github.isBlank()) return ValidationResult(true) // GitHub 可以为空
        
        return when {
            github.length > 39 -> ValidationResult(false, "GitHub 用户名最多39个字符")
            github.startsWith("-") || github.endsWith("-") -> 
                ValidationResult(false, "GitHub 用户名不能以连字符开头或结尾")
            github.contains("--") -> 
                ValidationResult(false, "GitHub 用户名不能包含连续的连字符")
            !github.matches(Regex("^[a-zA-Z0-9-]+$")) -> 
                ValidationResult(false, "GitHub 用户名只能包含字母、数字和连字符")
            else -> ValidationResult(true)
        }
    }
    
    /**
     * 验证微博用户名
     * 规则：4-30个字符
     */
    fun validateWeibo(weibo: String): ValidationResult {
        if (weibo.isBlank()) return ValidationResult(true) // 微博可以为空
        
        return when {
            weibo.length < 4 -> ValidationResult(false, "微博用户名至少4个字符")
            weibo.length > 30 -> ValidationResult(false, "微博用户名最多30个字符")
            else -> ValidationResult(true)
        }
    }
}

/**
 * 验证结果数据类
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String = ""
)
