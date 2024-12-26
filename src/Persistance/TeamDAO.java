package Persistance;

import Business.Team.Team;

import java.util.List;

public interface TeamDAO {
    // Agregar las firmas de los metodos para team

    public List<Team> getTeams();
    public void createTeam(Team team);
    public Team getTeam(String name);
    public boolean deleteTeam(Team team);


}
