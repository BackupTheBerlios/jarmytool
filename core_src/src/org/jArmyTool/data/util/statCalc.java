/*
 * statCalc.java
 *
 * Created on August 26, 2004, 6:45 PM
 */
/**
 *
 * @author  Oscar Almer
 */

package org.jArmyTool.data.util;

import org.jArmyTool.data.dataBeans.util.ModelStat;
import java.text.NumberFormat;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;

public class statCalc {
    public statCalc stat1;
    public statCalc stat2;
    public statCalc parent;
    private String val;
    private double numval;
    private boolean numerical;
    private boolean filled;
    /** Creates a new instance of statCalc */
    public statCalc(statCalc toClone)
    {
        this.filled = toClone.filled;
        this.numerical = toClone.numerical;
        this.numval = toClone.numval;
        this.val = toClone.val;
        this.parent = null;
        if(this.stat1 != null)
            this.stat1 = new statCalc(toClone.stat1, this);
        if(this.stat2 != null)
            this.stat2 = new statCalc(toClone.stat2, this);
    }
    
    public statCalc(statCalc toClone, statCalc parent)
    {
        this.filled = toClone.filled;
        this.numerical = toClone.numerical;
        this.numval = toClone.numval;
        this.val = toClone.val;
        this.parent = parent;
        if(this.stat1 != null)
            this.stat1 = new statCalc(toClone.stat1, this);
        if(this.stat2 != null)
            this.stat2 = new statCalc(toClone.stat2, this);
    }
    
    public statCalc(String val, statCalc parent) 
    {
       this.parent = parent;
       this.val = val;
       this.numerical = false;
       this.filled = false;
    }
    
    public statCalc(double val, statCalc parent)
    {
        this.parent = parent;
        this.numval = val;
        this.numerical = true;
        this.filled = false;
    }
    
    public void parse(String s)
    {
        int retval = 0;
        StringReader r = new StringReader(s);
        StreamTokenizer tokens = new StreamTokenizer(r);

        tokens.wordChars('a', 'Z');
        tokens.wordChars('(',')');
        tokens.wordChars('_', '_');
        tokens.parseNumbers();
        tokens.ordinaryChar('+');
        tokens.ordinaryChar('-');
        tokens.ordinaryChar('*');
        tokens.ordinaryChar('/');
        tokens.ordinaryChar('(');
        tokens.ordinaryChar(')');
        tokens.ordinaryChar(' ');
        tokens.quoteChar('"');
        
        try 
        {
            parse(tokens);
        }
        catch(java.io.IOException e)
        {
            
        }
    }
    private void parse(StreamTokenizer tokens) throws java.io.IOException
    {
        while(tokens.nextToken() != StreamTokenizer.TT_EOF)
        {
            if(tokens.ttype == StreamTokenizer.TT_WORD)
            {
             //   System.out.print("Read token:");
             //   System.out.println(tokens.sval);
                if(!this.filled)
                {
                    this.stat1 = new statCalc(tokens.sval, this);
                    this.filled = true;
                }
                else
                {
                    this.stat2 = new statCalc(tokens.sval, this);
                    this.stat2.parse(tokens);
                }
            }
            if(tokens.ttype == '"')
            {
             //   System.out.print("Read token:");
             //   System.out.println(tokens.sval);
                if(!this.filled)
                {
                    this.stat1 = new statCalc(tokens.sval, this);
                    this.filled = true;
                }
                else
                {
                    this.stat2 = new statCalc(tokens.sval, this);
                    this.stat2.parse(tokens);
                }
            }
            if(tokens.ttype == StreamTokenizer.TT_NUMBER)
            {
            //    System.out.print("Read token:");
            //    System.out.println(tokens.nval);
                if(this.filled)
                {
                    this.stat2 = new statCalc(tokens.nval, this);
                    this.stat2.parse(tokens);
                }
                else
                {
                    this.stat1 = new statCalc(tokens.nval, this);
                    this.filled = true;
                }
            }
            operatorMatch(tokens, '+');
            operatorMatch(tokens, '-');
            operatorMatch(tokens, '*');
            operatorMatch(tokens, '/');
            operatorMatch(tokens, '(');
            operatorMatch(tokens, ')');
        }
    }
 
    private void operatorMatch(StreamTokenizer tokens, char operator) throws java.io.IOException
    {
            if(tokens.ttype == operator)
                if(!filled)
                {
                    if(this.numerical)
                    {
                        stat1 = new statCalc(this.numval, this);
                        this.numerical = false;
                    }
                    else
                        stat1 = new statCalc(this.val, this);
                    this.val = String.valueOf(operator);
                    this.filled = true;
                }
                else
                {
                    if(val.equalsIgnoreCase("") && !this.numerical)
                    {
                        this.val = String.valueOf(operator);
                    }
                    else
                    {
                        this.stat2 = new statCalc(String.valueOf(operator), this);
                        this.stat2.parse(tokens);
                    }
                }        
    }
    
    public void debug()
    {
        if(stat1 != null)
        {
            System.out.print("left leg: "); 
                stat1.debug();
        }
            
        if(this.numerical)
            System.out.println(numval);
        else
            System.out.println(val);
        if(stat2 != null)
        {
                System.out.print("right leg: "); 
                stat2.debug();
        }
    }
    
    public boolean isNumerical()
    {
        return this.numerical;
    }
    
    public void replaceStats(Map replacements)
    {
        if(this.stat1 != null)
            this.stat1.replaceStats(replacements);
        if(this.stat2 != null)
            this.stat2.replaceStats(replacements);
        
        if(this.numerical)
            return;
        
        Iterator i = replacements.entrySet().iterator();
        while(i.hasNext())
        {
            Entry ent = (Entry)i.next();
            if(this.val.equalsIgnoreCase((String)ent.getKey()))
            {
                String srepl = (String)ent.getValue();
                try
                {
                    double nrepl = Double.valueOf(srepl).doubleValue();
                    this.numval = nrepl;
                    this.numerical = true;
                }
                catch (NumberFormatException e)
                {
                    this.val = srepl;
                    this.numerical = false;
                }
            }
        }
    }
    
    public void calculate()
    {
        if(this.stat1 != null)
            this.stat1.calculate();
        if(this.stat2 != null)
            this.stat2.calculate();
        if(this.stat1 != null && this.stat2 != null)
        if(this.stat1.isNumerical() && this.stat2.isNumerical())
        {
            
            if(val.equalsIgnoreCase("+"))
            {
                this.numval = stat1.nvalue() + stat2.nvalue();
                this.numerical = true;
                this.stat1 = null;
                this.stat2 = null;
            }
            if(val.equalsIgnoreCase("-"))
            {
                this.numval = stat1.nvalue() - stat2.nvalue();
                this.numerical = true;
                this.stat1 = null;
                this.stat2 = null;
            }
            if(val.equalsIgnoreCase("*"))
            {
                this.numval = stat1.nvalue() * stat2.nvalue();
                this.numerical = true;
                this.stat1 = null;
                this.stat2 = null;
            }
            if(val.equalsIgnoreCase("/"))
            {
                this.numval = stat1.nvalue() / stat2.nvalue();
                this.numerical = true;
                this.stat1 = null;
                this.stat2 = null;
            }
            
        }
        if(this.stat1 != null)
        if(val.equalsIgnoreCase(")") && this.stat1.isNumerical())
            {
                if(this.parent.stat1 != null & this.parent.stat1.isNumerical())
                {
                    this.parent.stat2 = this.stat1;
                    this.stat1 = null;
                    this.parent.calculate();
                    this.parent.stat2 = this;
                }
                
            }
        else
            return;
    }
    
    public double nvalue()
    {
        return this.numval;
    }
    
    public String getStat()
    {
        String ret = "";
        if(this.stat1 != null)
        {
            ret = ret.concat(this.stat1.getStat());
        }
        if(this.numerical)
        {
            ret = ret.concat(NumberFormat.getIntegerInstance().format(this.numval));
        }
        else
        {
            ret = ret.concat(this.val);
        }
        if(this.stat2 != null)
        {
            ret = ret.concat(this.stat2.getStat());
        }
        return ret;
    }
    
    public void parse(String s, Map m)
    {
        this.parse(s);
        this.replaceStats(m);
        this.calculate();
    }
    
    public static void main(String args[])
    {
        statCalc s = new org.jArmyTool.data.util.statCalc("", null);
        statCalc s2 = new org.jArmyTool.data.util.statCalc(s);
        String exp = "oldvalue(oldvalue+1)oldvalue*.2";
        String exp2 = "2+oldvalue*12";
        HashMap map = new java.util.HashMap();
        map.put("oldvalue", "4");
        map.put("Toughness", "3");
        
        s.parse(exp, map);
        s2.parse(exp2, map);
        
        statCalc s3 = new statCalc(s2);
        
        System.out.println(exp);
        System.out.println(s.getStat());
        System.out.println(exp2);
        System.out.println(s2.getStat());
        System.out.println(exp2);
        System.out.println(s3.getStat());
    }
}
