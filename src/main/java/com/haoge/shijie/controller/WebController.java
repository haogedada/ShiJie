package com.haoge.shijie.controller;


import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
//    private static final Logger LOGGER = LogManager.getLogger(WebController.class);
//
//    private UserService userService;
//
//    @Autowired
//    public void setService(UserService userService) {
//        this.userService = userService;
//    }
    //登入
//    @PostMapping("/login")
//    public ResponseBean login(@RequestParam("username") String username,
//                              @RequestParam("password") String password) {
//        UserBean userBean = userService.getUser(username);
//        if (userBean.getPassword().equals(password)) {
//            return new ResponseBean(200, "Login success", JWTUtil.sign(username, password));
//        } else {
//            throw new UnauthorizedException();
//        }
//    }
//    @GetMapping("/article") 所有人都可以访问，但是用户与游客看到的内容不同
//    public ResponseBean article() {
//        Subject subject = SecurityUtils.getSubject();
//        if (subject.isAuthenticated()) {//判断是游客还是用户
    //用户
//            return new ResponseBean(200, "You are already logged in", null);
//        } else {
    //游客
//            return new ResponseBean(200, "You are guest", null);
//        }
//    }
//    @GetMapping("/require_auth")
//    @RequiresAuthentication 登入的用户才可以进行访问
//    public ResponseBean requireAuth() {
//        return new ResponseBean(200, "You are authenticated", null);
//    }
//    @GetMapping("/require_role")
//    @RequiresRoles("admin") admin的角色用户才可以登入
//    public ResponseBean requireRole() {
//        return new ResponseBean(200, "You are visiting require_role", null);
//    }
//    @GetMapping("/require_permission")
//    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"}) 拥有view和edit权限的用户才可以访问
//    public ResponseBean requirePermission() {
//        return new ResponseBean(200, "You are visiting permission require edit,view", null);
//    }
//
//    @RequestMapping(path = "/401")
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ResponseBean unauthorized() {
//        return new ResponseBean(401, "Unauthorized", null);
//    }

}
