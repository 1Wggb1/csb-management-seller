package br.com.casasbahia.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
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
import br.com.casasbahia.service.SellerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping( "/v1/sellers" )
public class SellerController
{
    @Autowired
    private SellerService service;

    @GetMapping
    public SellerPageableDTO find(
        final String filter )
    {
        return null;
    }

    @PostMapping
    public ResponseEntity<SellerResponseDTO> createSeller(
        @Valid @RequestBody final SellerRequestDTO sellerDTO )
    {
        final SellerResponseDTO sellerResponseDTO = service.create( sellerDTO );
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .buildAndExpand( sellerResponseDTO.enrollment() )
            .toUri();
        return ResponseEntity.created( location ).body( sellerResponseDTO );
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
