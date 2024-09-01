package iasi.CP2.controller;

import java.util.ArrayList;
import java.util.List;
import iasi.CP2.model.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import iasi.CP2.model.Brinquedo;
import iasi.CP2.repository.BrinquedoRepositorio;


@CrossOrigin(origins = "https://exerciciorevisaojava-1.onrender.com")
@Controller
@RequestMapping("/view")
public class BrinquedoController {
    @Autowired
    BrinquedoRepositorio brinquedoRepositorio;

    @GetMapping("/brinquedos")
    public String listAllBrinquedos(@RequestParam(required = false) String nome, Model model) {
        List<Brinquedo> brinquedos = new ArrayList<>();
        try {
            if (nome == null) {
                brinquedoRepositorio.findAll().forEach(brinquedos::add);
            } else {
                brinquedoRepositorio.findByNameContaining(nome).forEach(brinquedos::add);
            }

            model.addAttribute("brinquedos", brinquedos);
            return "list"; // Nome do arquivo HTML para listar brinquedos
        } catch (Exception e) {
            model.addAttribute("error", new ErrorResponse("Erro ao listar brinquedos.", e.getMessage()));
            return "error";
        }
    }

    @GetMapping("/brinquedos/{id}")
    public String getBrinquedoById(@PathVariable("id") long id, Model model) {
        Brinquedo brinquedo = brinquedoRepositorio.findById(id);
        if (brinquedo != null) {
            model.addAttribute("brinquedo", brinquedo);
            return "detail"; // Nome do arquivo HTML para detalhes do brinquedo
        } else {
            model.addAttribute("error", new ErrorResponse("Brinquedo não encontrado.", "ID: " + id));
            return "error"; // Nome do arquivo HTML para exibir erro
        }
    }

    @GetMapping("/brinquedos/add")
    public String showAddBrinquedoForm(Model model) {
        model.addAttribute("brinquedo", new Brinquedo());
        return "add"; // Nome do arquivo HTML para adicionar brinquedos
    }

    @PostMapping("/brinquedos")
    public String createBrinquedo(@RequestBody Brinquedo brinquedo) {
        try {
            brinquedoRepositorio.save(brinquedo);
            return "redirect:/view/brinquedos"; // Redireciona para a lista de brinquedos
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/brinquedos/edit/{id}")
    public String showEditBrinquedoForm(@PathVariable("id") long id, Model model) {
        Brinquedo brinquedo = brinquedoRepositorio.findById(id);
        if (brinquedo != null) {
            model.addAttribute("brinquedo", brinquedo);
            return "edit"; // Nome do arquivo HTML para editar brinquedos
        } else {
            model.addAttribute("error", new ErrorResponse("Brinquedo não encontrado.", "ID: " + id));
            return "error";
        }
    }

    @PutMapping("/brinquedos/edit/{id}")
    public String updateBrinquedo(@PathVariable("id") long id, @RequestBody Brinquedo brinquedo) {
        Brinquedo _brinquedo = brinquedoRepositorio.findById(id);
        if (_brinquedo != null) {
            _brinquedo.setNome(brinquedo.getNome());
            _brinquedo.setTipo(brinquedo.getTipo());
            _brinquedo.setClassificacao(brinquedo.getClassificacao());
            _brinquedo.setTamanho(brinquedo.getTamanho());
            _brinquedo.setPreco(brinquedo.getPreco());

            brinquedoRepositorio.save(_brinquedo);
            return "redirect:/view/brinquedos";
        } else {
            return "error";
        }
    }

    @PostMapping("/brinquedos/delete/{id}")
    public String deleteBrinquedo(@PathVariable("id") long id) {
        try {
            brinquedoRepositorio.deleteById(id);
            return "redirect:/view/brinquedos"; // Redireciona para a lista de brinquedos
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/brinquedos/delete-all")
    public String deleteAllBrinquedos() {
        try {
            brinquedoRepositorio.deleteAll();
            return "redirect:/view/brinquedos"; // Redireciona para a lista de brinquedos
        } catch (Exception e) {
            return "error";
        }
    }

}
