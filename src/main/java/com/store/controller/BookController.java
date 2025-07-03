package com.store.controller;

import com.store.entity.Book;
import com.store.entity.MyBookList;
import com.store.entity.Usuario;
import com.store.services.BookService;
import com.store.services.MyBookService;
import com.store.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/")

public class BookController {

    @Autowired
    private BookService bookService ;
    @Autowired
    private MyBookService myBookService ;

    private final UsuarioService usuarioService;

    public BookController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public ModelAndView home1(){
        //acceso a la cookie de seguridad en el servidor
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        String userName = "";
        //Comprobamos si hay usuario logeado
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            userName = "anonimo@anonimo";
        }
        else {
            userName = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            //Si quiero el id el usuario necesito llamara a la base de datos
            Usuario usuario = usuarioService.getRepo().findUsuarioByEmail(userName);
            System.out.println("El id del usuario es:");
            System.out.println(usuario.getId());
        }
        System.out.println("El mombre de usuario es :" + userName );


        return new ModelAndView("home");
    }
    @GetMapping("/home")
    public ModelAndView home(){
        //acceso a la cookie de seguridad en el servidor
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        String userName = "";
        //Comprobamos si hay usuario logeado
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
            userName = "anonimo@anonimo";
        }
        else {
            userName = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        }
        System.out.println("El mombre de usuario es :" + userName );
        return new ModelAndView("home");
    }
    @GetMapping("/book_register")
    public ModelAndView bookRegister(){
        return new ModelAndView("bookRegister") ;
    }

    @GetMapping("/available_books")
    public ModelAndView getAllBooks(){
        List<Book> list = bookService.getAllBooks() ;
        return   new ModelAndView("/availableBooks", "book" , list);
    }

    @GetMapping("/my_books")
    public ModelAndView myBooks(){
        List<MyBookList> myList = myBookService.getAllBooks() ;
        return new ModelAndView("/myBooks", "myBook" , myList);
    }
//
    @PostMapping("/save")
    public ModelAndView addBook(@ModelAttribute Book book){
        bookService.save(book);
        return new ModelAndView("redirect:/available_books") ;
    }

    @RequestMapping("/list/{id}")
    public ModelAndView getMyList(@PathVariable("id") int id){
        Book book = bookService.findById(id) ;
        MyBookList myBookList = new MyBookList(book.getId() , book.getName(),book.getAuthor() ,book.getPrice());
        myBookService.saveMyBooks(myBookList);
        return new ModelAndView("redirect:/my_books") ;
    }
    @RequestMapping("/deleteBook/{id}")
    public ModelAndView deleteList(@PathVariable("id") int id) {
        bookService.deleteById(id);
        return new ModelAndView("redirect:/available_books") ;
    }

    @RequestMapping("/editBook/{id}")
    public ModelAndView editList(@PathVariable("id") int id, Model model) {
        Book b = bookService.findById(id) ;
        model.addAttribute("book" , b) ;
        bookService.deleteById(id);
        return new ModelAndView("editBook") ;
    }

}
