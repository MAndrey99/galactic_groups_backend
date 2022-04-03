package com.galactic_groups.controllers;

import com.galactic_groups.data.dto.OrganizationInfo;
import com.galactic_groups.data.dto.UserInfo;
import com.galactic_groups.data.model.Organization;
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
@RequestMapping("/api/organization")
@AllArgsConstructor
@Validated
public class OrganizationController {
    private final UserService userService;

    @Operation(summary = "Create new user", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new organization",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Organization.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(OnCreate.class)
    public Organization createOrganization(@RequestBody @Valid Organization org) {
        return userService.createOrganization(org);
    }

    @Operation(summary = "Get current organization info", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfo.class)))
    })
    @GetMapping(path = "/current")
    public OrganizationInfo getCurrentOrganizationInfo() {
        return userService.getCurrentOrganizationInfo();
    }

    // возможно, потом их не только создавать можно будет... Но пока и этого хватит.
}
