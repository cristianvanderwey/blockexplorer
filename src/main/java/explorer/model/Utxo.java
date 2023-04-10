package explorer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

@Document(collection = "utxos")
public class Utxo {
    @Indexed
    private String address;

    @Id
    private UtxoId utxoId;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal value;

    private Boolean spent;

    private String spentTxId;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UtxoId getUtxoId() {
        return utxoId;
    }

    public void setUtxoId(UtxoId utxoId) {
        this.utxoId = utxoId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Boolean getSpent() {
        return spent;
    }

    public void setSpent(Boolean spent) {
        this.spent = spent;
    }

    public String getSpentTxId() {
        return spentTxId;
    }

    public void setSpentTxId(String spentTxId) {
        this.spentTxId = spentTxId;
    }

    public Utxo(String address, UtxoId utxoId, BigDecimal value, Boolean spent, String spentTxId) {
        this.address = address;
        this.utxoId = utxoId;
        this.value = value;
        this.spent = spent;
        this.spentTxId = spentTxId;
    }

    private Utxo(){}



}


