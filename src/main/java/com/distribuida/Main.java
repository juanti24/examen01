package com.distribuida;

import com.distribuida.db.Book;
import com.distribuida.services.BookService;
import io.helidon.webserver.WebServer;
import jakarta.enterprise.inject.spi.CDI;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;

import java.util.List;
import com.google.gson.Gson;


public class Main {
    private static ContainerLifecycle lifecycle = null;

    public static void main(String[] args)  {
        lifecycle = WebBeansContext.currentInstance().getService(ContainerLifecycle.class);
        lifecycle.startApplication(null);
        BookService servicio = CDI.current().select(BookService.class).get();

        WebServer server = WebServer.builder()
                .port(8080)
                .routing(builder -> builder
                        .get("/books", (req, res) -> {
                            List<Book> books = servicio.findAll();
                            String response = new Gson().toJson(books);
                            res.send(response);
                        })
                        .get("/books/{id}",(req, res) -> {
                            Book b1=servicio.findById(Integer.valueOf(req.path().pathParameters().get("id")));
                            res.send(new Gson().toJson(b1));
                        })
                        .post("/books", (req, res) -> {
                            Gson gson=new Gson();
                            String body = req.content().as(String.class);
                            Book book1 = gson.fromJson(body, Book.class);
                            servicio.insert(book1);
                            res.send("Se ha insertado el libro");
                        })
                        .put("/books/{id}",(req, res) -> {
                            Gson gson=new Gson();
                            String body = req.content().as(String.class);
                            Book book1 = gson.fromJson(body, Book.class);
                            Integer id = Integer.valueOf(req.path().pathParameters().get("id"));
                            book1.setId(id);
                            servicio.update(book1);
                            res.send("Se ha actualizado el libro con id" + id);
                        })
                        .delete("/books/{id}",(req, res) -> {
                            Integer id = Integer.valueOf(req.path().pathParameters().get("id"));
                            servicio.delete(id);
                            res.send("Se ha eliminado el libro con id" + id);
                        })
                )
                .build();

        server.start();


        servicio.findAll().stream().forEach(System.out::println);
        shutdown();


    }

    public static void shutdown()  {
        lifecycle.stopApplication(null);
    }
}