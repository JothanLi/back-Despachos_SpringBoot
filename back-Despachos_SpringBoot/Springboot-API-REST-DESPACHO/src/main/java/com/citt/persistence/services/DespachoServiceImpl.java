package com.citt.persistence.services;

import com.citt.exceptions.DespachoNotFoundException;
import com.citt.persistence.entity.Despacho;
import com.citt.persistence.repository.DespachoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DespachoServiceImpl implements DespachoService {

    private final DespachoRepository despachoRepository;

    public DespachoServiceImpl(DespachoRepository despachoRepository) {
        this.despachoRepository = despachoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Despacho> findAllDespachos() {
        return despachoRepository.findAll();
    }

    @Override
    public Despacho saveDespacho(Despacho despacho) {
        despacho.setIdDespacho(null);
        if (despacho.getIntento() == null) {
            despacho.setIntento(0);
        }
        if (despacho.getDespachado() == null) {
            despacho.setDespachado(false);
        }
        return despachoRepository.save(despacho);
    }

    @Override
    public Despacho updateDespacho(Long idDespacho, Despacho despacho) throws DespachoNotFoundException {
        Despacho existente = findById(idDespacho);
        existente.setFechaDespacho(despacho.getFechaDespacho());
        existente.setPatenteCamion(despacho.getPatenteCamion());
        existente.setIntento(despacho.getIntento());
        existente.setIdCompra(despacho.getIdCompra());
        existente.setDireccionCompra(despacho.getDireccionCompra());
        existente.setValorCompra(despacho.getValorCompra());
        existente.setDespachado(despacho.getDespachado());
        return despachoRepository.save(existente);
    }

    @Override
    public Despacho updateEstado(Long idDespacho, Integer intento, Boolean despachado) throws DespachoNotFoundException {
        Despacho existente = findById(idDespacho);
        existente.setIntento(intento);
        existente.setDespachado(despachado);
        return despachoRepository.save(existente);
    }

    @Override
    public void deleteDespacho(Long idDespacho) throws DespachoNotFoundException {
        Despacho despacho = findById(idDespacho);
        despachoRepository.delete(despacho);
    }

    @Override
    @Transactional(readOnly = true)
    public Despacho findById(Long idDespacho) throws DespachoNotFoundException {
        return despachoRepository.findById(idDespacho)
                .orElseThrow(() -> new DespachoNotFoundException("Despacho no encontrado con ID: " + idDespacho));
    }
}
