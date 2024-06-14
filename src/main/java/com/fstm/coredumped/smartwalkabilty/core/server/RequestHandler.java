package com.fstm.coredumped.smartwalkabilty.core.server;

import com.fstm.coredumped.smartwalkabilty.common.controller.*;
import com.fstm.coredumped.smartwalkabilty.common.model.service.response.BasicResponse;
import com.fstm.coredumped.smartwalkabilty.core.danger.controller.DangerCtrl;
import com.fstm.coredumped.smartwalkabilty.core.danger.model.bo.Declaration;
import com.fstm.coredumped.smartwalkabilty.core.geofencing.model.bo.Geofencing;
import com.fstm.coredumped.smartwalkabilty.core.routing.model.bo.Routage;
import com.fstm.coredumped.smartwalkabilty.web.Controller.DTOS.ResponseDTO;
import com.fstm.coredumped.smartwalkabilty.web.Model.bo.Site;

import java.util.List;

public class RequestHandler {

    public Object handleRequest(Object req) throws IllegalArgumentException {
        if (req instanceof DangerReq) {
            return handleDangerRequest((DangerReq) req);
        } else if (req instanceof ShortestPathReq) {
            return handleShortestPathRequest((ShortestPathReq) req);
        } else if (req instanceof ShortestPathWithAnnounces) {
            return handleShortestPathWithAnnounces((ShortestPathWithAnnounces) req);
        } else if (req instanceof RequestPerimetreAnnonce) {
            return handleRequestPerimetreAnnonce((RequestPerimetreAnnonce) req);
        } else if (req instanceof DeclareDangerReq) {
            handleDeclareDangerRequest((DeclareDangerReq) req);
            return true;
        } else if (req instanceof ReserveRequest) {
            return handleReserveRequest((ReserveRequest) req);
        } else if (req instanceof CommentRequest) {
            return handleCommentRequest((CommentRequest) req);
        } else {
            throw new IllegalArgumentException("Unknown request type");
        }
    }

    private List<Declaration> handleDangerRequest(DangerReq req) {
        return new DangerCtrl().requestDangers(req);
    }

    private Object handleShortestPathRequest(ShortestPathReq req) {
        Routage routage = new Routage(req);
        routage.calculerChemins();
        return routage.getChemins();
    }

    private Object handleShortestPathWithAnnounces(ShortestPathWithAnnounces req) {
        Routage routage = new Routage(req);
        routage.calculerChemins();
        return routage.getChemins();
    }

    private List<Site> handleRequestPerimetreAnnonce(RequestPerimetreAnnonce req) {
        return Geofencing.findAllAnnoncesByRadius(req.getActualPoint(), req.getPerimetre(), req.getCategorieList());
    }

    private void handleDeclareDangerRequest(DeclareDangerReq req) {
        new DangerCtrl().danger_ctrl(req);
    }

    private ResponseDTO handleReserveRequest(ReserveRequest req) {
        return new ReservationController().handle(req);
    }

    private BasicResponse<Integer> handleCommentRequest(CommentRequest req) {
        return new CommentsController().comment(req);
    }

}
