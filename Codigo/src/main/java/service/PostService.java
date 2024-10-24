package service;

import dao.PostDAO;
import model.Post;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class PostService {

    private PostDAO postDAO;
    private Gson gson = new Gson();

    public PostService() {
        postDAO = new PostDAO();
        postDAO.conectar();
    }

    public Object add(Request request, Response response) {
        JsonObject dados = gson.fromJson(request.body(), JsonObject.class);

        if (dados == null) {
            response.status(400); // HTTP 400 Bad Request
            return gson.toJson("Erro: Requisição inválida, corpo do JSON está vazio.");
        }

        if (!dados.has("conteudo") || dados.get("conteudo").isJsonNull()) {
            response.status(400);
            return gson.toJson("Erro: Campo 'conteudo' está faltando.");
        }
        if (!dados.has("data") || dados.get("data").isJsonNull()) {
            response.status(400);
            return gson.toJson("Erro: Campo 'data' está faltando.");
        }
        if (!dados.has("imagem") || dados.get("imagem").isJsonNull()) {
            response.status(400);
            return gson.toJson("Erro: Campo 'imagem' está faltando.");
        }
        if (!dados.has("id_usuario") || dados.get("id_usuario").isJsonNull()) {
            response.status(400);
            return gson.toJson("Erro: Campo 'id_usuario' está faltando.");
        }
        if (!dados.has("id_local") || dados.get("id_local").isJsonNull()) {
            response.status(400);
            return gson.toJson("Erro: Campo 'id_local' está faltando.");
        }

        try {
            String conteudo = dados.get("conteudo").getAsString();
            String dataString = dados.get("data").getAsString();
            String imagem = dados.get("imagem").getAsString();
            int id_usuario = dados.get("id_usuario").getAsInt();
            int id_local = dados.get("id_local").getAsInt();

            Post post = new Post(-1, conteudo, dataString, imagem, id_usuario, id_local);
            postDAO.inserirPost(post);

            response.status(201); // HTTP 201 Created
            return gson.toJson("Post inserido com sucesso!");

        } catch (Exception e) {
            response.status(500); // HTTP 500 Internal Server Error
            return gson.toJson("Erro ao inserir o post: " + e.getMessage());
        }
    }




    public Object get(Request request, Response response) {
        int id_post = Integer.parseInt(request.params(":id"));
        Post post = postDAO.getPost(id_post);

        if (post != null) {
            response.header("Content-Type", "application/json");
            response.header("Content-Encoding", "UTF-8");
            return gson.toJson(post);
        } else {
            response.status(404); // HTTP 404 Not found
            return gson.toJson("Post " + id_post + " não encontrado.");
        }
    }

    public Object update(Request request, Response response) {
        int id_post = Integer.parseInt(request.params(":id"));
        JsonObject data = gson.fromJson(request.body(), JsonObject.class);

        Post postExistente = postDAO.getPost(id_post);

        if (postExistente != null) {
            String conteudo = data.has("conteudo") ? data.get("conteudo").getAsString().trim() : postExistente.getConteudo();
            String dataString = data.has("data") ? data.get("data").getAsString().trim() : postExistente.getData(); // Mantido como String
            String imagem = data.has("imagem") ? data.get("imagem").getAsString().trim() : postExistente.getImagem();
            int id_usuario = data.has("id_usuario") ? data.get("id_usuario").getAsInt() : postExistente.getId_usuario();
            int id_local = data.has("id_local") ? data.get("id_local").getAsInt() : postExistente.getId_local();

            postExistente.setConteudo(conteudo);
            postExistente.setData(dataString);
            postExistente.setImagem(imagem);
            postExistente.setId_usuario(id_usuario);
            postExistente.setId_local(id_local);

            postDAO.atualizarPost(postExistente);

            response.status(200); // HTTP 200 OK
            return gson.toJson("Post atualizado com sucesso!");
        } else {
            response.status(404); // HTTP 404 Not found
            return gson.toJson("Post não encontrado.");
        }
    }

    public Object remove(Request request, Response response) {
        int id_post = Integer.parseInt(request.params(":id"));

        boolean status = postDAO.excluirPost(id_post);

        if (status) {
            response.status(200); // HTTP 200 OK
            return gson.toJson("Post removido com sucesso!");
        } else {
            response.status(404); // HTTP 404 Not found
            return gson.toJson("Post não encontrado.");
        }
    }

    public Object getAll(Request request, Response response) {
        Post[] arrayPosts = postDAO.getPosts();
        List<Post> posts = new ArrayList<>(Arrays.asList(arrayPosts));

        response.header("Content-Type", "application/json");
        response.header("Content-Encoding", "UTF-8");

        return gson.toJson(posts);
    }
}

