package app;

import config.MyConfiguration;
import database.GamesDAO;
import database.UserDAO;
import filter.AuthenticationFilter;
import io.dropwizard.auth.AuthValueFactoryProvider;
import resources.GamesResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import model.Games;
import model.User;
import resources.UserResource;
import services.UserService;


public class MyApplication extends Application<MyConfiguration> {

    private static MyConfiguration myConfiguration;

    public static void main(String[] args) throws Exception {
        new MyApplication().run(args);
    }

    @Override
    public String getName() {
        return "games";
    }



    private final HibernateBundle<MyConfiguration> hibernate = new HibernateBundle<MyConfiguration>(Games.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(MyConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    private final MigrationsBundle migrationsBundle = new MigrationsBundle<MyConfiguration>() {
        @Override
        public DataSourceFactory getDataSourceFactory(MyConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<MyConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
        bootstrap.addBundle(migrationsBundle);
    }

    @Override
    public void run(MyConfiguration configuration,
                    Environment environment) {
        myConfiguration = configuration;




        final GamesDAO gamesDAO = new GamesDAO(hibernate.getSessionFactory());

        final GamesResource gamesResource = new GamesResource(gamesDAO);
        environment.jersey().register(gamesResource);
//        environment.jersey().register(new AuthDynamicFeature(
//                new BasicCredentialAuthFilter.Builder<User>()
//                        .setAuthorizer(new GamesAuthorizer())
//                        .setRealm("SECURITY REALM")
//                        .buildAuthFilter()));


        //User stuff
        final UserDAO userDAO = new UserDAO();
        final UserService authenticationService = new UserService(userDAO);
        final UserResource authenticationResource = new UserResource(authenticationService);
        final AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationService);
        environment.jersey().register(new AuthDynamicFeature(authenticationFilter));
        environment.jersey().register(authenticationResource);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));



    }

    public static MyConfiguration getMyConfiguration() {
        return myConfiguration;
    }


}
