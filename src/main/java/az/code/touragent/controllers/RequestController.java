package az.code.touragent.controllers;

import az.code.touragent.models.Request;
import az.code.touragent.services.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/request")
public class RequestController {
    RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Request> getRequest(@PathVariable UUID requestId){
        return ResponseEntity.ok(requestService.getRequest(requestId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Request>> getAllRequests(){
        return ResponseEntity.ok(requestService.getAllRequests());
    }
}
