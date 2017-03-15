/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.autocep.esper.queries;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import java.awt.Color;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.autocep.esper.Esper;
import me.autocep.esper.models.Event;
import me.autocep.global.Global;

/**
 *
 * @author Raef
 */
public class MonitoringQueries {

    public static void query1(Esper esper) {
        String query = "SELECT * FROM " + esper.getStream();
        EPStatement statement = esper.getService().getEPAdministrator().createEPL(query);
        statement.addListener((EventBean[] ebs, EventBean[] ebs1) -> {
            Event w = (Event) ebs[0].getUnderlying();
            Field[] fields = w.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    String name = field.getName();
                    Object value = field.get(w);
                    Global.writeLog(esper.getMain().getMonitoringPane(), Color.BLACK, name + ": " + value);
                }catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(MonitoringQueries.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Global.drawLine(esper.getMain().getMonitoringPane(), Color.BLUE);
        });
    }

}
