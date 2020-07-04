package API.crudProduct;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DatabaseUpdater {

    @Autowired
    private DatabaseUpdates databaseUpdates;

    public void resetID() {
        this.databaseUpdates.resetID();
    }


}
