package broccoli.controllers;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/vertex")
public class VertexController {

    @Get(produces="text/plain")
    public String index() {
        return "Hello, Vertex";
    }
}
