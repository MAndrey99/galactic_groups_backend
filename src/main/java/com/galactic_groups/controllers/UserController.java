package com.galactic_groups.controllers;

import com.galactic_groups.data.dto.UserInfo;
import com.galactic_groups.data.model.User;
import com.galactic_groups.data.validation.OnCreate;
import com.galactic_groups.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @Operation(summary = "Create new user", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new user",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfo.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(OnCreate.class)
    public UserInfo createUser(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }

    @Operation(summary = "Delete a user by its id", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted", content = @Content),
            @ApiResponse(responseCode = "204", description = "User does not exist", content = @Content)
    })
    @DeleteMapping(path = "/{id}")
    public void deleteStudentById(@PathVariable long id) {
        userService.deleteUserById(id);
    }

    @Operation(summary = "Get current user info", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfo.class)))
    })
    @GetMapping(path = "/current")
    public UserInfo getCurrentUserInfo() {
        return userService.getCurrentUserInfo();
    }
}
