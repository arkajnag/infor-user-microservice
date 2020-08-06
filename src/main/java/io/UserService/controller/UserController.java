package io.UserService.controller;

import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.UserService.exceptionhandler.DataNotFoundException;
import io.UserService.exceptionhandler.DuplicateDataNotAllowedException;
import io.UserService.exceptionhandler.NullFormatException;
import io.UserService.model.User;
import io.UserService.model.UserDTO;
import io.UserService.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "User API Service", consumes = "application/json",produces = "application/json")
@RestController(value = "User Controller")
@RequestMapping("/rest/user")
public class UserController {
	
	
	@Autowired
	private UserService userService;
	
    @ApiOperation(value = "Create new User Information", httpMethod = "POST", consumes = "application/json",produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200,message = "Successful"),
            @ApiResponse(code = 400,message = "BAD Request"),
            @ApiResponse(code = 401,message = "Unauthorized"),
            @ApiResponse(code = 404,message = "Page Not Found"),
            @ApiResponse(code = 500,message = "Server Error")
    })
	@PostMapping(value = "/new/user", consumes = "application/json", produces = "application/json")
	public CompletableFuture<ResponseEntity<User>> registerNewUserInformation(@RequestBody(required = true)User userObj) throws DataNotFoundException, NullFormatException, DuplicateDataNotAllowedException {
		return userService.registerNewUserInfo(userObj).thenApply(userResponseObj->ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(userResponseObj));
	}
	
	@ApiOperation(value = "GET All User Information", httpMethod = "GET", consumes = "application/json",produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200,message = "Successful"),
            @ApiResponse(code = 400,message = "BAD Request"),
            @ApiResponse(code = 401,message = "Unauthorized"),
            @ApiResponse(code = 404,message = "Page Not Found"),
            @ApiResponse(code = 500,message = "Server Error")
    })
	@GetMapping(value="/all",produces = "application/json")
	public CompletableFuture<ResponseEntity<UserDTO>> getAllUsersInformation() throws DataNotFoundException {
		return userService.getAllUserInformation().thenApplyAsync(userDTOObj->ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(userDTOObj));
	}
	
    @ApiOperation(value = "GET User Information By specific UserID", httpMethod = "GET", consumes = "application/json",produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200,message = "Successful"),
            @ApiResponse(code = 400,message = "BAD Request"),
            @ApiResponse(code = 401,message = "Unauthorized"),
            @ApiResponse(code = 404,message = "Page Not Found"),
            @ApiResponse(code = 500,message = "Server Error")
    })
	@GetMapping(value="/userid/{id}",produces = "application/json")
	public CompletableFuture<ResponseEntity<User>> getUserInformationById(@PathVariable(required = true,value = "id")Integer id) throws DataNotFoundException, NullFormatException {
		return userService.getUserInformationById(id).thenApply(userResponseObj->ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(userResponseObj));
	}
	
    @ApiOperation(value = "GET User Information By specific Username", httpMethod = "GET", consumes = "application/json",produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200,message = "Successful"),
            @ApiResponse(code = 400,message = "BAD Request"),
            @ApiResponse(code = 401,message = "Unauthorized"),
            @ApiResponse(code = 404,message = "Page Not Found"),
            @ApiResponse(code = 500,message = "Server Error")
    })
	@GetMapping(value="/username/{username}",produces = "application/json")
	public CompletableFuture<ResponseEntity<User>> getUserInformationByUsername(@PathVariable(required = true,value="username")String username) throws DataNotFoundException, NullFormatException {
		return userService.getUserInformationByUsername(username).thenApply(userResponseObj->ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(userResponseObj));
	}
    
    @GetMapping(value="/all/reports/{type}", produces = "application/json")
    public String getUserReport(@PathVariable(value = "type", required = true)String type) throws DataNotFoundException{
        Object response=userService.exportUserReport(type);
        return (response==null)?"User Report file is created!!":"Error in generating the Report";
    }
}
