package explorer.repository;

import explorer.model.Utxo;
import explorer.model.UtxoId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface UtxoRepository extends MongoRepository<Utxo, Long> {

     Optional<Utxo> findByUtxoId(UtxoId UtxoId);

     ArrayList<Utxo> findByAddress(String address);

}
