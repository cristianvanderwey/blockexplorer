package explorer.repository;

import explorer.model.BlockCounter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlockCounterRepository extends MongoRepository<BlockCounter, String> {


}
