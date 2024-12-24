package com.example.islab1new.controllers;

import com.example.islab1new.dao.ImportHistoryDAO;
import com.example.islab1new.models.history.ImportHistory;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/import")
public class ImportController {

    @Inject
    private ImportHistoryDAO importHistoryDAO;
    @GET
    @Path("/history")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ImportHistory> getAllHistory(){
        return importHistoryDAO.findAllHistory();
    }
}
