# Fine-grained-permission-control
a demo about fine-grained permission control started with springboot

- 这个demo中包含了两个拦截器和一个生成角色路径的工具类
- 权限路径拦截器`AuthInterceptor`可以控制角色访问的路径
- 客制化拦截器`CustomInterceptor`可以控制角色访问路径前经过的增强方法
- 两者均通过配置文件动态配置
