package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FareCalculatorService {

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:" +ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        long msDuration = outHour - inHour;
        long secondsDuration = msDuration / 1000;
        long minuteDuration = (msDuration / 1000) / 60;
        long hourDuration = minuteDuration / 60;
        System.out.println("Heure d'arrivée : " + inHour);
        System.out.println("Heure de départ : " + outHour);
        System.out.println("msDuration = " + msDuration);
        System.out.println("Durée de stationnement en minutes : " + minuteDuration);
        System.out.println("Durée de stationnement en heures : " + hourDuration);
        System.out.println("Durée de stationnement en secondes : " + secondsDuration);

        boolean isLoyal = isLoyal(ticket);

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                if(minuteDuration <= 30){
                    ticket.setPrice(0);
                    System.out.println("Durée de stationnement inférieure à 30 minutes, stationnement gratuit");
                    break;
                }

                if(isLoyal){
                    ticket.setPrice((minuteDuration * Fare.CAR_RATE_PER_MINUTE) * 0.95);
                    System.out.println("===== Réduction fidélité =====");
                    System.out.println("===== 5% de Remise =====");
                    break;
                }
                ticket.setPrice(minuteDuration * Fare.CAR_RATE_PER_MINUTE);
                break;
            }
            case BIKE: {

                if(minuteDuration <= 30){
                    ticket.setPrice(0);
                    System.out.println("Durée de stationnement inférieure à 30 minutes, stationnement gratuit");
                    break;
                }

                if(isLoyal){
                    ticket.setPrice((minuteDuration * Fare.BIKE_RATE_PER_MINUTE) * 0.95);
                    System.out.println("===== Réduction fidélité =====");
                    System.out.println("===== 5% de Remise =====");
                    break;
                }
                ticket.setPrice(minuteDuration * Fare.BIKE_RATE_PER_MINUTE);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    public boolean isLoyal(Ticket ticket){
        //if vehicle_reg_number présent + de 5 fois, accorder 5% de remise
        String registerNumber = ticket.getVehicleRegNumber();
        Connection con = null;
        ResultSet rs = null;
        int count = 0;
        try{
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.IS_CLIENT_LOYAL);
            ps.setString(1, registerNumber);
            rs = ps.executeQuery();
            if(rs.next()){
                count = rs.getInt(1);
            }

            System.out.println("Nombre de fois que ce véhicule a utilisé notre parking : " + count);
            if(count >= 5){
                return true;
            }else{
                return false;
            }
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }

    }
}