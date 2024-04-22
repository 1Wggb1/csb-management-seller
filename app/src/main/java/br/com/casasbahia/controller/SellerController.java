package br.com.casasbahia.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.casasbahia.dto.SellerDTO;
import br.com.casasbahia.dto.SellerPageableDTO;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.dto.SellerResponseDTO;
import br.com.casasbahia.service.SellerService;
import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;

@RestController
@RequestMapping( "/v1/sellers" )
public class SellerController
{
    @Autowired
    private SellerService service;

    @Observed( name = "seller.findAll" )
    @GetMapping
    public ResponseEntity<SellerPageableDTO> findAll(
        @RequestParam( name = "filter", required = false ) final String filter,
        @RequestParam( name = "page", defaultValue = "0" ) final Integer page,
        @RequestParam( name = "size", defaultValue = "10" ) final Integer size )
    {
        final PageRequest pageable = PageRequest.of( page, size );
        return ResponseEntity.ok( service.findAll( filter, pageable ) );
    }

    @Observed( name = "seller.findByEnrollment" )
    @GetMapping( "/{enrollment}" )
    public ResponseEntity<SellerDTO> findByEnrollment(
        @PathVariable( "enrollment" ) final String enrollment )
    {
        return ResponseEntity.ok( service.findByEnrollment( enrollment ) );
    }

    @Observed( name = "seller.create" )
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

    @Observed( name = "seller.update" )
    @PutMapping( "/{enrollment}" )
    public ResponseEntity<SellerResponseDTO> update(
        @PathVariable( "enrollment" ) final String enrollment,
        @Valid @RequestBody final SellerRequestDTO sellerRequestDTO )
    {
        service.update( enrollment, sellerRequestDTO );
        return ResponseEntity.noContent().build();
    }

    @Observed( name = "seller.delete" )
    @DeleteMapping( "/{enrollment}" )
    public ResponseEntity<Void> delete(
        @PathVariable( "enrollment" ) final String enrollment )
    {
        service.delete( enrollment );
        return ResponseEntity.noContent().build();
    }
}
