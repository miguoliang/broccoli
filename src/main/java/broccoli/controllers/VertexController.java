package broccoli.controllers;

import io.micronaut.http.annotation.*;
import io.micronaut.security.authentication.Authentication;

@Controller("/vertex")
public class VertexController {

    @Get(uri="/", produces="text/plain")
    public String index(Authentication authentication) {
        return authentication.getName();
    }
}