import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


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


        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new GamesAuthenticator(configuration.getLogin(),
                                configuration.getPassword()))
                        .setRealm("SECURITY REALM")
                        .buildAuthFilter()));

    }

    public static MyConfiguration getMyConfiguration() {
        return myConfiguration;
    }


}
