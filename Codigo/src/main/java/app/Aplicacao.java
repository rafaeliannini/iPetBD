package app;

import static spark.Spark.*;

import service.UsuarioService;
import service.PostService;
import service.EmpresaService;
import service.LocalService;
import service.ProdutoService;

public class Aplicacao {
    private static UsuarioService usuarioService = new UsuarioService();
    private static PostService postService = new PostService();
    private static EmpresaService empresaService = new EmpresaService();
    private static LocalService localService = new LocalService();
    private static ProdutoService produtoService = new ProdutoService();
    
    private static void enableCORS(final String origin, final String methods, final String headers) {
        options("/*", (request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Allow-Methods", methods);
            response.header("Access-Control-Allow-Headers", headers);
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Allow-Methods", methods);
            response.header("Access-Control-Allow-Headers", headers);
            if ("OPTIONS".equals(request.requestMethod())) {
                response.status(200);
                halt();
            }
        });
    }


    
    public static void main(String[] args) {
        port(80);
        enableCORS("*", "GET, POST, PUT, DELETE, OPTIONS", "Content-Type, Authorization");
        
        // Rotas para Usuario
        post("/usuario", (request, response) -> usuarioService.add(request, response));
        get("/usuario/:id", (request, response) -> usuarioService.get(request, response));
        put("/usuario/:id", (request, response) -> usuarioService.update(request, response));
        delete("/usuario/:id", (request, response) -> usuarioService.remove(request, response));
        get("/usuarios", (request, response) -> usuarioService.getAll(request, response));
        post("usuario/login", (request, response) -> usuarioService.login(request, response));
        // Rotas para Post
        post("/post", (request, response) -> postService.add(request, response));
        get("/post/:id", (request, response) -> postService.get(request, response));
        put("/post/:id", (request, response) -> postService.update(request, response));
        delete("/post/:id", (request, response) -> postService.remove(request, response));
        get("/posts", (request, response) -> postService.getAll(request, response));
        // Rotas para Empresa
        post("/empresa", (request, response) -> empresaService.add(request, response));
        get("/empresa/:id", (request, response) -> empresaService.get(request, response));
        put("/empresa/:id", (request, response) -> empresaService.update(request, response));
        delete("/empresa/:id", (request, response) -> empresaService.remove(request, response));
        get("/empresas", (request, response) -> empresaService.getAll(request, response));
        post("empresa/login", (request, response) -> empresaService.login(request, response));
        // Rotas para Local
        post("/local", (request, response) -> localService.add(request, response));
        get("/local/:id", (request, response) -> localService.get(request, response));
        put("/local/:id", (request, response) -> localService.update(request, response));
        delete("/local/:id", (request, response) -> localService.remove(request, response));
        get("/locais", (request, response) -> localService.getAll(request, response));
        // Rotas para Produto
        post("/produto", (request, response) -> produtoService.add(request, response));
        get("/produto/:id", (request, response) -> produtoService.get(request, response));
        put("/produto/:id", (request, response) -> produtoService.update(request, response));
        delete("/produto/:id", (request, response) -> produtoService.remove(request, response));
        get("/produtos", (request, response) -> produtoService.getAll(request, response));
    }

}	
