import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

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
    @Path("/")
    @UnitOfWork
    public List<Games> findAll(@Auth User user) {
        return gamesDAO.findAll();
    }

    @GET
    @Path("/{name}")
    @UnitOfWork
    public List<Games> findByName(@PathParam("name") String name, @Auth User user) {
        return gamesDAO.findByName(name);

    }

    @GET
    @Path("/findById")
    @UnitOfWork
    public List<Games> findById(@QueryParam("id") long id, @Auth User user) {
        return gamesDAO.findById(id);

    }

    @POST
    @Path("/addGame/")
    @UnitOfWork
    public void addGame(@Auth User user, @Valid Games games) {
        gamesDAO.addGame(games);
    }

    @PUT
    @Path("/updateGame/")
    @UnitOfWork
    public void updateGame(@Valid Games games) {
        gamesDAO.updateGame(games);
    }

    @DELETE
    @Path("/deleteGame/{id}")
    @UnitOfWork
    public void deleteGame(@PathParam("id") long id, @Auth User user) {
        gamesDAO.deleteGame(id);
    }
}
