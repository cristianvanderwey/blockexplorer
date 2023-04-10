package explorer.controller;
import explorer.model.AddressBalance;
import explorer.services.BlockExplorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class BlockExplorerController {

    @Autowired
    BlockExplorerService blockExplorerService;

    @GetMapping("/api/address-info")
    public AddressBalance getAddressInfo(@RequestParam String address) {
        return blockExplorerService.getAddressBalance(address);
    }

}
