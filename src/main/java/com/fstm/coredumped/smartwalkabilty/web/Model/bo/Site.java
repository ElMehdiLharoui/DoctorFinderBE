package com.fstm.coredumped.smartwalkabilty.web.Model.bo;

import com.fstm.coredumped.smartwalkabilty.common.utils.TimeCalcules;
import com.fstm.coredumped.smartwalkabilty.common.model.bo.GeoPoint;
import com.fstm.coredumped.smartwalkabilty.common.model.bo.Reservation;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.fstm.coredumped.smartwalkabilty.common.utils.DateHandling.equalsByDateOnly;

public class Site implements Serializable
{
    private static final long serialVersionUID = 123456789L;
    private int id;
    private String Name;
    private String Address;
    private Date dateCreated;
    private Organisation organisation;
    private GeoPoint localisation;
    private int dureeviste, maxpatient;
    private String heuredebut,heurefin;
    private String weekday;
    private Collection<Reservation> reservations;
    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }


    public int getDureeviste() {
        return dureeviste;
    }

    public void setDureeviste(int dureeviste) {
        this.dureeviste = dureeviste;
    }

    public int getMaxpatient() {
        return maxpatient;
    }

    public void setMaxpatient(int maxpatient) {
        this.maxpatient = maxpatient;
    }

    public String getHeuredebut() {
        return heuredebut;
    }

    public void setHeuredebut(String heuredebut) {
        this.heuredebut = heuredebut;
    }

    public String getHeurefin() {
        return heurefin;
    }

    public void setHeurefin(String heurefin) {
        this.heurefin = heurefin;
    }

    public String getAdress() {
        return Address;
    }

    public void setAdress(String address) {
        Address = address;
    }
    private List<Annonce> annonces = new LinkedList<Annonce>();

    public List<Annonce> getAnnonces() {
        return annonces;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }


    public void setLocalisation(GeoPoint gp) {
        this.localisation = gp;
    }


    public GeoPoint getLocalisation() {
        return localisation;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Site() {
    }

    public Site(String name, Date dateCreated) {
        Name = name;
        this.dateCreated = dateCreated;
    }
    public void AddAnnonce(Annonce annonce)
    {
        annonces.add(annonce);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site site = (Site) o;
        return id == site.id;
    }
    public Site CreateCopyWithoutAnnonce_organisation()
    {
        Site site = new Site(this.Name,this.dateCreated);
        site.setLocalisation(this.localisation);
        site.setId(this.id);
        return site;
    }


    public Collection<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Collection<Reservation> reservations) {
        this.reservations = reservations;
    }
    public List<Integer> calculateAvailableTimesToReserveInMinutes(Date date)
    {
        int currentTime = TimeCalcules.getCurrentTimeInMinutes();
        List<Integer> minutesReservations = reservations.stream().filter(x-> equalsByDateOnly(x.getDate(),date)).map(Reservation::getTempDeReservation).map(TimeCalcules::stringHourToMinutes).sorted().collect(Collectors.toList());
        List<Integer> allTimesInDay = calculateAllReservationTimes();
        if(!equalsByDateOnly(date,new Date())){
            return allTimesInDay.stream().filter(x-> !minutesReservations.contains(x)).collect(Collectors.toList());
        }
        return allTimesInDay.stream().filter(x-> !minutesReservations.contains(x) && x >= currentTime).collect(Collectors.toList());
    }
    public List<String> calculateAvailableTimesToReserveInStringHours(Date date)
    {
        return calculateAvailableTimesToReserveInMinutes(date).stream().map(TimeCalcules::minutesToStringHour).collect(Collectors.toList());
    }
    public List<Integer> calculateAllReservationTimes(){
        int startTime = TimeCalcules.stringHourToMinutes(heuredebut);
        int endTime = TimeCalcules.stringHourToMinutes(heurefin);
        List<Integer> times = new ArrayList<>();
        for (int i = startTime; i < endTime; i+= dureeviste) {
            times.add(i);
        }
        return times;
    }
}
