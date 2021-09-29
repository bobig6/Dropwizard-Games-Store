package database;

import database.GamesDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import model.Games;
import model.User;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/games")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class GamesResource {
    GamesDAO gamesDAO;

    public GamesResource(GamesDAO gamesDAO) {
        this.gamesDAO = gamesDAO;
    }

    @GET
    @UnitOfWork
    public List<Games> findAll(@QueryParam("name") String name, @Auth User user) {
        if(name != null){
            return gamesDAO.findByName(name);
        }
        return gamesDAO.findAll();
    }


    @GET
    @Path("/{id}")
    @UnitOfWork
    public List<Games> findById(@PathParam("id") long id, @Auth User user) {

        return gamesDAO.findById(id);

    }

    @POST
    @RolesAllowed("ADMIN")
    @UnitOfWork
    public void addGame(@Valid Games games) {
        gamesDAO.addGame(games);
    }

    @PUT
    @RolesAllowed("ADMIN")
    @UnitOfWork
    public void updateGame(@Valid Games games) {
        gamesDAO.updateGame(games);
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public void deleteGame(@PathParam("id") long id, @Auth User user) {
        gamesDAO.deleteGame(id);
    }
}
