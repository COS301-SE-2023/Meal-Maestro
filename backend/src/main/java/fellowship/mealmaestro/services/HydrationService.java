package fellowship.mealmaestro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.neo4j.UserModel;
import fellowship.mealmaestro.models.neo4j.relationships.HasLogEntry;
import fellowship.mealmaestro.repositories.neo4j.UserRepository;
import java.util.List;

@Service
public class HydrationService {
    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 20 * 60 * 1000)
    public void pollLogs() {
        List<UserModel> userList = userRepository.findUsersWithNewLogEntries();
        //per user
        for(UserModel user : userList){
            List<HasLogEntry> logEntries = userRepository.findUnprocessedLogEntriesForUser(user);
            for(HasLogEntry entry : logEntries)
            {
                
            }
        }

    }
   
}
