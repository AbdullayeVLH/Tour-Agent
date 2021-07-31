package az.code.touragent.controllers;

import az.code.touragent.dtos.UserData;
import az.code.touragent.exceptions.AlreadyArchived;
import az.code.touragent.exceptions.RequestExpired;
import az.code.touragent.models.Request;
import az.code.touragent.services.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/request")
public class RequestController {
    RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @ExceptionHandler(RequestExpired.class)
    public ResponseEntity<String> handleNotFound(RequestExpired e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyArchived.class)
    public ResponseEntity<String> handleNotFound(AlreadyArchived e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Request> getRequest(@PathVariable UUID requestId, @RequestAttribute("user") UserData user){
        return ResponseEntity.ok(requestService.getRequest(requestId, user.getEmail()));
    }

    @GetMapping("/offered")
    public ResponseEntity<List<Request>> getOfferedRequests(@RequestAttribute("user") UserData user){
        return ResponseEntity.ok(requestService.getOfferedRequests(user.getEmail()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Request>> getAllRequests(@RequestAttribute("user")UserData user){
        return ResponseEntity.ok(requestService.getAllRequests(user.getEmail()));
    }

    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity deleteRequest(@RequestAttribute("user")UserData user, @PathVariable UUID requestId){
        return ResponseEntity.ok(requestService.deleteRequest(requestId, user.getEmail()));
    }

    @PostMapping("/archive/{requestId}")
    public ResponseEntity sendToArchive(@RequestAttribute("user")UserData user, @PathVariable UUID requestId){
        return ResponseEntity.ok(requestService.sendToArchive(requestId, user.getEmail()));
    }

    @GetMapping("/archives")
    public ResponseEntity<List<Request>> getArchivedRequests(@RequestAttribute("user")UserData user){
        return ResponseEntity.ok(requestService.getArchiveRequests(user.getEmail()));
    }
}
