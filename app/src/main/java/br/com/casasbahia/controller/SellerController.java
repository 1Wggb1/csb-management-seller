package br.com.casasbahia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.casasbahia.dto.SellerPageableDTO;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.dto.SellerResponseDTO;

@RestController
@RequestMapping( "/v1/sellers" )
public class SellerController
{

    @GetMapping
    public SellerPageableDTO find(
        final String filter )
    {
        return null;
    }

    @PostMapping
    public ResponseEntity<SellerResponseDTO> createSeller(
        @RequestBody final SellerRequestDTO sellerDTO )
    {
        return ResponseEntity.created( ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand( "" ).toUri() ).body( null );
    }

    @PutMapping
    public ResponseEntity<SellerResponseDTO> update(
        @RequestBody final SellerRequestDTO sellerDTO )
    {
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
        final String enrollment )
    {
        return ResponseEntity.noContent().build();
    }
}
