import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "games")
@NamedQueries({
        @NamedQuery(name = "Games.findAll",
                query = "select e from Games e"),
        @NamedQuery(name = "Games.findByName",
                query = "select e from Games e "
                        + "where e.gameName like :name "),
        @NamedQuery(name = "Games.addGame",
                query = "insert into Games(gameName, gameGenre) select :gameName, :gameGenre from Games")
})
public class Games {
    @Id
    @Column(name = "gameId", nullable = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    @Column(name = "gameName")
    @JsonProperty
    private String gameName;

    @Column(name = "gameGenre")
    @JsonProperty
    private String gameGenre;

    public Games(){

    }

    public Games(String gameName, String gameGenre) {
        this.gameName = gameName;
        this.gameGenre = gameGenre;
    }

    public Games(long id, String  name, String genre){
        this.gameId = id;
        this.gameName = name;
        this.gameGenre = genre;
    }


    public Long getGameId() {
        return gameId;
    }
    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGameGenre(String gameGenre) {
        this.gameGenre = gameGenre;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGameGenre() {
        return gameGenre;
    }

    @Override
    public String toString() {
        return "Games{" +
                "gameId=" + gameId +
                ", gameName='" + gameName + '\'' +
                ", gameGenre='" + gameGenre + '\'' +
                '}';
    }
}
