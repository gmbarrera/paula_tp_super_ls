package Persistance;

import Business.Team.Team;

import java.util.List;

public interface TeamDAO {
    public List<Team> getTeams();
    public void createTeam(Team team);
    public Team getTeam(String name);
    public boolean deleteTeam(Team team);
}
