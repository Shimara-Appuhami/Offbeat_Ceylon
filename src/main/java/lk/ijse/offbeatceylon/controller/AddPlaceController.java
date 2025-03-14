package lk.ijse.offbeatceylon.controller;

import lk.ijse.offbeatceylon.dto.AddPlaceDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/addPlace")
@CrossOrigin("*")
public class AddPlaceController {
    private List<AddPlaceDTO> places=new ArrayList<>();

    @PostMapping(path = "/save")
    public AddPlaceDTO addPlace(@RequestBody AddPlaceDTO addPlaceDTO) {
        places.add(addPlaceDTO);
        return addPlaceDTO;

    }
}
