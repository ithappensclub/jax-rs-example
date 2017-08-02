package com.welyab.ithappens.tutorials.springboot.jaxrs.rest.endpoint.users;

import com.welyab.ithappens.tutorials.springboot.jaxrs.data.model.LinkInfo;
import com.welyab.ithappens.tutorials.springboot.jaxrs.data.model.User;
import com.welyab.ithappens.tutorials.springboot.jaxrs.service.UserAlreadyRegisteredException;
import com.welyab.ithappens.tutorials.springboot.jaxrs.service.UserNotFoundException;
import com.welyab.ithappens.tutorials.springboot.jaxrs.service.UserService;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("users")
public class UsersResource {

    @Autowired
    private UserService userService;

    @POST
    @Consumes({
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML
    })
    @Produces({
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML
    })
    public Response save(@Context UriInfo uriInfo, User user) {
        try {
            User savedUser = userService.save(user);
            setLinks(uriInfo, user);
            return Response
                    .accepted(savedUser)
                    .build();
        } catch (UserAlreadyRegisteredException e) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity(String.format("The user with unique information %s already registered", e.getMessage()))
                    .build();
        } catch (Exception e) {
            throw new WebApplicationException(
                    e,
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()
            );
        }
    }

    @PUT
    @Consumes({
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML
    })
    @Produces({
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML
    })
    public Response edit(@Context UriInfo uriInfo, User user) {
        try {
            user = userService.update(user);
            setLinks(uriInfo, user);
            return Response.accepted(user).build();
        } catch (Exception e) {
            throw new WebApplicationException(
                    e,
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()
            );
        }
    }

    @GET
    @Produces({
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML
    })
    public Response find(
            @Context UriInfo uriInfo,
            @MatrixParam("id") List<Long> ids,
            @MatrixParam("name") List<String> names,
            @MatrixParam("email") List<String> emails
    ) {
        try {
            List<User> users = userService.find(ids, names, emails);
            for (User user : users) {
                setLinks(uriInfo, user);
            }
            UserList userList = new UserList();
            userList.setUsers(users);
            return Response
                    .ok(userList)
                    .build();
        } catch (Exception e) {
            throw new WebApplicationException(
                    e,
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()
            );
        }
    }

    @GET
    @Path("{id}")
    @Produces({
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML
    })
    public Response find(
            @Context UriInfo uriInfo,
            @PathParam("id") Long id) {
        try {
            User user = userService.find(id);
            setLinks(uriInfo, user);
            return Response
                    .ok(user)
                    .build();
        } catch (UserNotFoundException ex) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(String.format("User not found for id %d", id))
                    .build();
        } catch (Exception e) {
            throw new WebApplicationException(
                    e,
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()
            );
        }
    }

    @DELETE
    @Path("{id}")
    @Produces({
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML
    })
    public Response delete(@PathParam("id") Long idUser) {
        userService.delete(idUser);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    private static void setLinks(UriInfo uriInfo, User user) throws NoSuchMethodException {
        user.setLinks(Arrays.asList(
                LinkInfo.create("GET", createFindLink(uriInfo, user)),
                LinkInfo.create("PUT", createEditLink(uriInfo, user)),
                LinkInfo.create("DELETE", createDeleteLink(uriInfo, user))
        ));
    }

    private static Link createEditLink(UriInfo uriInfo, User user) throws NoSuchMethodException {
        URI deleteUri = uriInfo
                .getBaseUriBuilder()
                .path(UsersResource.class)
                .build();
        return Link.fromUri(deleteUri)
                .build();
    }

    private static Link createFindLink(UriInfo uriInfo, User user) throws NoSuchMethodException {
        URI deleteUri = uriInfo
                .getBaseUriBuilder()
                .path(UsersResource.class)
                .path(UsersResource.class.getMethod("find", UriInfo.class, Long.class))
                .build(user.getId());
        return Link.fromUri(deleteUri)
                .build();
    }

    private static Link createDeleteLink(UriInfo uriInfo, User user) throws NoSuchMethodException {
        URI deleteUri = uriInfo
                .getBaseUriBuilder()
                .path(UsersResource.class)
                .path(UsersResource.class.getMethod("delete", Long.class))
                .build(user.getId());
        return Link.fromUri(deleteUri)
                .build();
    }
}
