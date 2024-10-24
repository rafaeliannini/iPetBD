package service;

import dao.ProdutoDAO;
import model.Produto;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ProdutoService {

    private ProdutoDAO produtoDAO;
    private Gson gson = new Gson();

    public ProdutoService() {
        produtoDAO = new ProdutoDAO();
        produtoDAO.conectar();
    }

    public Object add(Request request, Response response) {
        JsonObject data = gson.fromJson(request.body(), JsonObject.class);
        
        String nome = data.get("nome").getAsString();
        String descricao = data.get("descricao").getAsString();
        String imagem = data.get("imagem").getAsString();
        double preco = data.get("preco").getAsDouble();
        String linkSite = data.get("link_site").getAsString();
        float notaMedia = data.get("nota_media").getAsFloat();
        int idEmpresa = data.get("id_empresa").getAsInt();

        Produto produto = new Produto(-1, nome, descricao, imagem, preco, linkSite, notaMedia, idEmpresa);
        produtoDAO.inserirProduto(produto);

        response.status(201);
        return "Produto " + nome + " inserido com sucesso!";
    }


    public Object get(Request request, Response response) {
        int id_produto = Integer.parseInt(request.params(":id"));

        Produto produto = produtoDAO.getProduto(id_produto);

        if (produto != null) {
            response.header("Content-Type", "application/json");
            response.header("Content-Encoding", "UTF-8");

            Gson gson = new Gson();
            return gson.toJson(produto);
        } else {
            response.status(404); // 404 Not found
            return gson.toJson("Produto " + id_produto + " não encontrado.");
        }
    }



    public Object update(Request request, Response response) {
        int id_produto = Integer.parseInt(request.params(":id"));

        JsonObject data = gson.fromJson(request.body(), JsonObject.class);

        Produto produtoExistente = produtoDAO.getProduto(id_produto);

        if (produtoExistente != null) {
            String nome = data.has("nome") ? data.get("nome").getAsString().trim() : produtoExistente.getNome();
            String descricao = data.has("descricao") ? data.get("descricao").getAsString().trim() : produtoExistente.getDescricao();
            String imagem = data.has("imagem") ? data.get("imagem").getAsString().trim() : produtoExistente.getImagem();
            double preco = data.has("preco") ? data.get("preco").getAsDouble() : produtoExistente.getPreco();
            String linkSite = data.has("link_site") ? data.get("link_site").getAsString().trim() : produtoExistente.getLink_site();
            float notaMedia = data.has("nota_media") ? data.get("nota_media").getAsFloat() : produtoExistente.getNota_media();
            int idEmpresa = data.has("id_empresa") ? data.get("id_empresa").getAsInt() : produtoExistente.getId_empresa();

            produtoExistente.setNome(nome);
            produtoExistente.setDescricao(descricao);
            produtoExistente.setImagem(imagem);
            produtoExistente.setPreco(preco);
            produtoExistente.setLink_site(linkSite);
            produtoExistente.setNota_media(notaMedia);
            produtoExistente.setId_empresa(idEmpresa);

            produtoDAO.atualizarProduto(produtoExistente);

            response.status(200);
            return gson.toJson("Produto atualizado com sucesso.");
        } else {
            response.status(404);
            return gson.toJson("Produto não encontrado.");
        }
    }






    public Object remove(Request request, Response response) {
        int id_produto = Integer.parseInt(request.params(":id"));

        boolean status = produtoDAO.excluirProduto(id_produto);

        if (status) {
            response.status(200); // 200 OK
            return "Produto removido com sucesso!";
        } else {
            response.status(404); // 404 Not found
            return "Produto não encontrado.";
        }
    }

    public Object getAll(Request request, Response response) {
        Produto[] arrayProdutos = produtoDAO.getProdutos();
        List<Produto> produtos = new ArrayList<>(Arrays.asList(arrayProdutos));
        
        Gson gson = new Gson();
        String produtosJson = gson.toJson(produtos);

        response.header("Content-Type", "application/json");
        response.header("Content-Encoding", "UTF-8");

        return produtosJson;
    }


}
