/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.primefaces.context.RequestContext;

/**
 *
 * @author AhmedJorge
 */
public class GenericExcel {

    static String reString;
    public static ArrayList<Object[]> list;
    public static HashMap<String, ArrayList<Object[]>> map;
    public static boolean no = false;
    public static String nomeNo;
    public static HashMap<String, ArrayList<Object[]>> mapTotal;
    static int linha = 0;
    static int i = 0, k = 0;
    public static Date dI;
    public static Date dF;
    public static RelatorioConverter.MapParam[] removeItem = new RelatorioConverter.MapParam[]{ };
    public static RelatorioConverter.MapParam[] showItem = new RelatorioConverter.MapParam[]{ };
    public static HashMap<Integer,String> renameItem = new HashMap<>();
    public static int paramFilterOculta = -1;
    public static HashMap<Integer,Alignment> alignment = new HashMap<>();
    private static String[] valoresTotal = new String[]{};
//    public static HashMap<Integer,String> mapValoresTotal = new HashMap();
    public static int[] arrValoresTotal = new int[]{};
    
    /**
     *
     * @param user
     * @param nomeDoc
     * @param titleDoc
     * @param rs
     * @param paramFilter
     * @return
     */
    public static String createDoc(String user, String nomeDoc, String titleDoc, ArrayList<Object[]> rs, int paramFilter) {

        OutputStream outputStraem;
        try {
            i = 0;
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy hh'.'mm'.'ss");
            File ff = new File(ConfigDoc.Fontes.getDiretorio() + "/" + user + "/Relatorio");
            ff.mkdirs();
            String Ddata = sdf1.format(new Date());
            ff = new File(ff.getAbsoluteFile() + "/" + nomeDoc + " " + Ddata + ".xls");
            outputStraem = new FileOutputStream(ff);
            reString = "../Documentos/" + user + "/Relatorio/" + nomeDoc + " " + Ddata + ".xls";

            getMap(rs, paramFilter);
            float[] colun = createPerncetage(list, paramFilter);
            System.out.println("Export.GenericExcel.createDoc() => "+Arrays.toString(colun));

            Workbook wb = new HSSFWorkbook();

            Font fTitulo = wb.createFont();
            fTitulo.setBoldweight(Font.BOLDWEIGHT_BOLD);
            fTitulo.setFontHeightInPoints((short) 14);

            Font fTituloP = wb.createFont();
            fTituloP.setBoldweight(Font.BOLDWEIGHT_BOLD);
            fTituloP.setFontHeightInPoints((short) 12);
//            fTituloP.setStrikeout(true);
            fTituloP.setUnderline(Font.U_SINGLE);

            Font fTituloTabela = wb.createFont();
            fTituloTabela.setBoldweight(Font.BOLDWEIGHT_BOLD);
            fTituloTabela.setFontHeightInPoints((short) 8);

            Font fCorpoTabela = wb.createFont();
            fCorpoTabela.setBoldweight(Font.BOLDWEIGHT_NORMAL);
            fCorpoTabela.setFontHeightInPoints((short) 8.5);

            Font fRodapeTabela = wb.createFont();
            fRodapeTabela.setBoldweight(Font.BOLDWEIGHT_BOLD);
            fRodapeTabela.setFontHeightInPoints((short) 8.5);

            Font fNormal = wb.createFont();
            fNormal.setBoldweight(Font.BOLDWEIGHT_BOLD);
            fNormal.setFontHeightInPoints((short) 8.5);

            CellStyle csTitulo = wb.createCellStyle();
            csTitulo.setFont(fTitulo);
            csTitulo.setAlignment((short) 1);
            csTitulo.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
            csTitulo.setBorderBottom((short) 0);
            csTitulo.setBorderTop((short) 0);
            csTitulo.setBorderRight((short) 0);
            csTitulo.setBorderLeft((short) 0);
            csTitulo.setWrapText(true);

            CellStyle csTituloP = wb.createCellStyle();
            csTituloP.setFont(fTituloP);
            csTituloP.setAlignment((short) 1);
            csTituloP.setVerticalAlignment((short) 1);
            csTituloP.setBorderBottom((short) 0);
            csTituloP.setBorderTop((short) 0);
            csTituloP.setBorderRight((short) 0);
            csTituloP.setBorderLeft((short) 0);
            csTituloP.setWrapText(true);

            CellStyle csTituloT = wb.createCellStyle();
            csTituloT.setFont(fTituloP);
            csTituloT.setAlignment((short) 1);
            csTituloT.setVerticalAlignment((short) 1);
            csTituloT.setBorderBottom((short) 0);
            csTituloT.setBorderTop((short) 0);
            csTituloT.setBorderRight((short) 0);
            csTituloT.setBorderLeft((short) 0);
            csTituloT.setWrapText(true);

            CellStyle csTituloTabela = wb.createCellStyle();
            csTituloTabela.setFont(fTituloTabela);
            csTituloTabela.setAlignment(CellStyle.ALIGN_CENTER);
            csTituloTabela.setVerticalAlignment((short) 2);
            csTituloTabela.setBorderBottom((short) 2);
            csTituloTabela.setBorderTop((short) 2);
            csTituloTabela.setBorderRight((short) 2);
            csTituloTabela.setBorderLeft((short) 2);
            csTituloTabela.setWrapText(true);
            
            CellStyle csTituloTabelaNBorder = wb.createCellStyle();
            csTituloTabelaNBorder.setFont(fTituloTabela);
            csTituloTabelaNBorder.setAlignment(CellStyle.ALIGN_CENTER);
            csTituloTabelaNBorder.setVerticalAlignment((short) 2);
            csTituloTabelaNBorder.setBorderBottom((short) 2);
            csTituloTabelaNBorder.setBorderTop((short) 2);
            csTituloTabelaNBorder.setBorderRight((short) 2);
            csTituloTabelaNBorder.setBorderLeft((short) 2);
            csTituloTabelaNBorder.setWrapText(true);

            CellStyle csCorpoTabela = wb.createCellStyle();
            csCorpoTabela.setFont(fCorpoTabela);
            csCorpoTabela.setAlignment((short) 2);
            csCorpoTabela.setVerticalAlignment((short) 1);
            csCorpoTabela.setBorderBottom((short) 1);
            csCorpoTabela.setBorderTop((short) 1);
            csCorpoTabela.setBorderRight((short) 1);
            csCorpoTabela.setBorderLeft((short) 1);
            csCorpoTabela.setWrapText(true);

            CellStyle csCorpoTabelaR = wb.createCellStyle();
            csCorpoTabelaR.setFont(fCorpoTabela);
            csCorpoTabelaR.setAlignment(CellStyle.ALIGN_RIGHT);
            csCorpoTabelaR.setVerticalAlignment((short) 1);
            csCorpoTabelaR.setBorderBottom((short) 1);
            csCorpoTabelaR.setBorderTop((short) 1);
            csCorpoTabelaR.setBorderRight((short) 1);
            csCorpoTabelaR.setBorderLeft((short) 1);
            csCorpoTabelaR.setWrapText(true);

            CellStyle csCorpoTabelaL = wb.createCellStyle();
            csCorpoTabelaL.setFont(fCorpoTabela);
            csCorpoTabelaL.setAlignment(CellStyle.ALIGN_LEFT);
            csCorpoTabelaL.setVerticalAlignment((short) 1);
            csCorpoTabelaL.setBorderBottom((short) 1);
            csCorpoTabelaL.setBorderTop((short) 1);
            csCorpoTabelaL.setBorderRight((short) 1);
            csCorpoTabelaL.setBorderLeft((short) 1);
            csCorpoTabelaL.setWrapText(true);
            

            CellStyle csRodapeTabelaL = wb.createCellStyle();
            csRodapeTabelaL.setFont(fRodapeTabela);
            csRodapeTabelaL.setAlignment(CellStyle.ALIGN_CENTER);
            csRodapeTabelaL.setVerticalAlignment((short) 2);
            csRodapeTabelaL.setBorderBottom((short) 2);
            csRodapeTabelaL.setBorderTop((short) 2);
            csRodapeTabelaL.setBorderRight((short) 2);
            csRodapeTabelaL.setBorderLeft((short) 2);
            csRodapeTabelaL.setWrapText(true);

            CellStyle csRodapeTabela = wb.createCellStyle();
            csRodapeTabela.setFont(fRodapeTabela);
            csRodapeTabela.setAlignment(CellStyle.ALIGN_CENTER);
            csRodapeTabela.setVerticalAlignment((short) 2);
            csRodapeTabela.setBorderBottom((short) 2);
            csRodapeTabela.setBorderTop((short) 2);
            csRodapeTabela.setBorderRight((short) 2);
            csRodapeTabela.setBorderLeft((short) 2);
            csRodapeTabela.setWrapText(true);

            CellStyle csRodapeTabelaR = wb.createCellStyle();
            csRodapeTabelaR.setFont(fRodapeTabela);
            csRodapeTabelaR.setAlignment(CellStyle.ALIGN_RIGHT);
            csRodapeTabelaR.setVerticalAlignment((short) 2);
            csRodapeTabelaR.setBorderBottom((short) 2);
            csRodapeTabelaR.setBorderTop((short) 2);
            csRodapeTabelaR.setBorderRight((short) 2);
            csRodapeTabelaR.setBorderLeft((short) 2);
            csRodapeTabelaR.setWrapText(true);

            CellStyle csNomal = wb.createCellStyle();
            csNomal.setFont(fCorpoTabela);
            csNomal.setAlignment((short) 1);
            csNomal.setVerticalAlignment((short) 1);
            csNomal.setBorderBottom((short) 0);
            csNomal.setBorderTop((short) 0);
            csNomal.setBorderRight((short) 0);
            csNomal.setBorderLeft((short) 0);
            csNomal.setWrapText(true);

            Sheet s = wb.createSheet(titleDoc);

            linha = 0;

            Row r = s.createRow(linha);
            Cell c = r.createCell(2);
            CreateCell(c, r, s, csTitulo, linha, linha + 3, ConfigDoc.Empresa.NOME, 1, 22);
            linha += 4;

            r = s.createRow(linha);
            CreateCell(c, r, s, csTituloP, linha, linha, ConfigDoc.Empresa.ENDERECO, 1, 22);
            linha++;

            r = s.createRow(linha);
            CreateCell(c, r, s, csTituloP, linha, linha, ConfigDoc.Empresa.CAIXAPOSTAL, 1, 22);
            linha++;

            r = s.createRow(linha);
            CreateCell(c, r, s, csTituloP, linha, linha, ConfigDoc.Empresa.TELEFAX + " " + ConfigDoc.Empresa.EMAIL, 1, 22);
            linha++;

            r = s.createRow(linha);
            CreateCell(c, r, s, csTituloP, linha, linha, ConfigDoc.Empresa.SOCIEDADE, 1, 22);
            linha += 3;

            r = s.createRow(linha);
            SimpleDateFormat format = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy",new Locale("pt", "BR"));
            CreateCell(c, r, s, csTituloT, linha, linha, titleDoc.toUpperCase()+((dF!=null&&dI!=null) ? (" de "+format.format(dI)+" à "+format.format(dF)).toUpperCase() : ""), 1, 22);
            linha += 2;
            if (paramFilter < 0) {
                int somak;
                for (Object[] emap : list) {
                    k = 0;
                    somak = 1;
//                if( colun[k] )
                    r = s.createRow(linha);
                    for (int j = 0; j < emap.length; j++) {
                        if (j != paramFilterOculta) {
                            if (i == 0) {
                                csCorpoTabela.setFillBackgroundColor(HSSFColor.BLUE.index);
                                CreateCell(c, r, s, csTituloTabela, linha, linha, toString(emap[j]), somak, (somak + toInt(colun[k]) - 1));
                                somak += toInt(colun[k]);
                                k++;
                            } else {
                                csCorpoTabelaL.setFillBackgroundColor(((i % 2) == 0) ? HSSFColor.WHITE.index : HSSFColor.GREY_25_PERCENT.index);
                                CreateCell(c, r, s, ((alignment.containsKey(j)) ? ((alignment.get(j) == Alignment.RIGHT) ? csCorpoTabelaR : ((alignment.get(j) == Alignment.CENTER) ? csCorpoTabela : csCorpoTabelaL)) : csCorpoTabelaL), linha, linha, toString(emap[j]), somak, (somak + toInt(colun[k]) - 1));
                                somak += toInt(colun[k]);
                                k++;
                            }
                        }
                    }
                    i++;
                    linha++;
                }
                for (Map.Entry<String, ArrayList<Object[]>> entrySet : mapTotal.entrySet()) {
                    for (Object[] emapT : entrySet.getValue()) {
                        k = 0;
                        somak = 1;
                        r = s.createRow(linha);
                        for (int j = 0; j < emapT.length; j++) {
                            if (j != paramFilterOculta) {
                                csCorpoTabelaL.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);
                                CreateCell(c, r, s, ((alignment.containsKey(j)) ? ((alignment.get(j) == Alignment.RIGHT) ? csRodapeTabelaR : ((alignment.get(j) == Alignment.CENTER) ? csRodapeTabelaR : csRodapeTabelaL)) : csRodapeTabelaL), linha, linha, ((j == 0) ? "TOTAL" : toString(emapT[j])), somak, (somak + toInt(colun[k]) - 1));
                                somak += toInt(colun[k]);
                                k++;
                            }
                        }
                    }
                }
            } else {
                int somak;
                int t = map.size();
                for (Map.Entry<String, ArrayList<Object[]>> lista : map.entrySet()) {
                    r = s.createRow(linha);
                    csTituloTabela.setFillBackgroundColor(HSSFColor.WHITE.index);
                    CreateCell(c, r, s, csTituloTabelaNBorder, linha, linha, toString(lista.getKey()), 1, 7);
                    linha+=2;
                    for (Object[] emap : lista.getValue()) {
                        k = 0;
                        somak = 1;
                        r = s.createRow(linha);
                        for (int j = 0; j < emap.length; j++) {
                            if (j != paramFilterOculta) {
                                if (i == 0) {
                                    csTituloTabela.setFillBackgroundColor(HSSFColor.BLUE.index);
                                    CreateCell(c, r, s, csTituloTabela, linha, linha, toString(emap[j]), somak, (somak + toInt(colun[k]) - 1));
                                    somak += toInt(colun[k]);
                                    k++;
                                } else {
                                    csCorpoTabelaL.setFillBackgroundColor(((i % 2) == 0) ? HSSFColor.WHITE.index : HSSFColor.GREY_25_PERCENT.index);
                                    CreateCell(c, r, s, ((alignment.containsKey(j)) ? ((alignment.get(j) == Alignment.RIGHT) ? csCorpoTabelaR : ((alignment.get(j) == Alignment.CENTER) ? csCorpoTabela : csCorpoTabelaL)) : csCorpoTabelaL), linha, linha, toString(emap[j]), somak, (somak + toInt(colun[k]) - 1));
                                    somak += toInt(colun[k]);
                                    k++;
                                }
                            }

                        }
                        i++;
                        linha++;
                    }

                    if (mapTotal.containsKey(lista.getKey())) {
                        for (Object[] emapT : mapTotal.get(lista.getKey())) {
                            k = 0;
                            somak = 1;
                            r = s.createRow(linha);
                            for (int j = 0; j < emapT.length; j++) {
                                if (j != paramFilterOculta) {
//                                    csCorpoTabelaL.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);
                                    CreateCell(c, r, s,((alignment.containsKey(j)) ? ((alignment.get(j) == Alignment.RIGHT) ? csRodapeTabelaR : ((alignment.get(j) == Alignment.CENTER) ? csRodapeTabela : csRodapeTabelaL)) : csRodapeTabelaL), linha, linha, ((j == 0) ? "TOTAL" : toString(emapT[j])), somak, (somak + toInt(colun[k]) - 1));
                                    somak += toInt(colun[k]);
                                    k++;
                                }
                            }
                        }
                    }
                    t++;
                    i = 0;
                    if (t != map.size()) {
                        linha += 3;
                    }
                }

            }

            wb.write(outputStraem);
          
            RequestContext.getCurrentInstance().execute("openAllDocument('" + reString + "')");
            no = false;
            nomeNo = "";
            dI = null;
            dF = null;
            paramFilterOculta = -1;
            removeItem = new RelatorioConverter.MapParam[]{ };
            showItem = new RelatorioConverter.MapParam[]{ };
            renameItem = new HashMap<>();
            alignment = new HashMap<>();
            valoresTotal = new String[]{};
//            mapValoresTotal = new HashMap();
            arrValoresTotal = new int[]{};
            return reString;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GenericExcel.class.getName()).log(Level.SEVERE, null, ex);
            return reString;
        } catch (IOException ex) {
            Logger.getLogger(GenericExcel.class.getName()).log(Level.SEVERE, null, ex);
            return reString;
        }
    }

    private static void getMap(ArrayList<Object[]> rs, int paramFilter) {
        list = new ArrayList<>();
        
        list.addAll(rs);
        
        if(arrValoresTotal.length>0)
            calTotalValores(list);

        if (paramFilter > -1) {
            map = new LinkedHashMap<>();
            for (int j = 1; j < list.size(); j++) {
//                System.err.println(list.get(j)[paramFilter] + "");
                if ((!no && !(list.get(j)[paramFilter] + "").equals(nomeNo) && !(list.get(j)[0] + "").trim().toUpperCase().equals("TOTAL"))
                        || (no && !(list.get(j)[paramFilter] + "").equals(nomeNo) && !(list.get(j)[0] + "").trim().toUpperCase().equals("TOTAL"))) {
                    if (map.containsKey(list.get(j)[paramFilter] + "")) {
                        map.get((list.get(j)[paramFilter]) + "").add(list.get(j));
                    } else {
                        if (list.get(j)[paramFilter] != null) {
                            ArrayList<Object[]> al = new ArrayList<>();
                            al.add(list.get(0));
                            al.add(list.get(j));
                            map.put(list.get(j)[paramFilter] + "", al);
                        }
                    }
                }
            }
        }

        mapTotal = new LinkedHashMap<>();
        for (int j = 1; j < list.size(); j++) {
            if ((list.get(j)[0] + "").toUpperCase().equals("TOTAL") && list.get(0).length > 1) {
                ArrayList<Object[]> al = new ArrayList<>();
                al.add(list.get(j));
                mapTotal.put((list.get(j)[1] + ""), al);
            }
        }
        
        if (paramFilter > -1) {
            map.entrySet().stream().forEach((entrySet) -> {
                String key = entrySet.getKey();
                ArrayList<Object[]> value = entrySet.getValue();
            map.replace(key,  (showItem.length > 0) ? showParamm(value) : removeParamm(value));
            });
        }
        mapTotal.entrySet().stream().forEach((entrySet) -> {
            String key = entrySet.getKey();
            ArrayList<Object[]> value = entrySet.getValue();
            mapTotal.replace(key,  (showItem.length > 0) ? showParamm(value) : removeParamm(value));
        });
        
        list =  (showItem.length > 0) ? showParamm(list) : removeParamm(list);
        
    }

    static int somaTotal = 0;

    private static float[] createPerncetage(ArrayList<Object[]> lista, int paramFilter) {

        if (lista == null || lista.isEmpty()) {
            return null;
        }
        float[] perncetages = new float[((paramFilter == -1) ? lista.get(0).length : lista.get(0).length - 1)];
        int[] lenthMax = new int[((paramFilter == -1) ? lista.get(0).length : lista.get(0).length - 1)];

        lista.stream().forEach((l) -> {
            int soma = 0, j = 0, ii = 0;
            for (Object emap : l) {
                if (paramFilter != ii) {
                    if (lenthMax[j] < toString(emap).length()) {
                        soma = soma - lenthMax[j];
                        lenthMax[j] = toString(emap).length();
                        somaTotal = somaTotal + lenthMax[j];
                    }
                    j++;
                }
                ii++;
            }

        });
//        for (int g = 0; g < perncetages.length; g++) {
//            perncetages[g] = toInt((((float) ((float) lenthMax[g] * 100) / somaTotal) > 30) ? (((float) ((float) lenthMax[g] * 20) / somaTotal))
//                    : (((float) ((float) lenthMax[g] * 100) / somaTotal) > 25) ? (((float) ((float) lenthMax[g] * 30) / somaTotal))
//                        : (((float) ((float) lenthMax[g] * 100) / somaTotal) > 20) ? (((float) ((float) lenthMax[g] * 20) / somaTotal))
//                            : (((float) ((float) lenthMax[g] * 100) / somaTotal) > 15) ? (((float) ((float) lenthMax[g] * 15) / somaTotal))
//                                : (((float) ((float) lenthMax[g] * 100) / somaTotal) > 10) ? (((float) ((float) lenthMax[g] * 20) / somaTotal))
//                                    : (((float) ((float) lenthMax[g] * 100) / somaTotal) > 5) ? ((float) ((float) lenthMax[g] * 30) / somaTotal)
//                                        : (((float) ((float) lenthMax[g] * 100) / somaTotal) < 1) ? 1 : ((float) ((float) lenthMax[g] * 100) / somaTotal));
//        }

        for (int g = 0; g < perncetages.length; g++) {
                    perncetages[g] = (((float) ((float) lenthMax[g] * 100) / somaTotal) > 30) ? (((float) ((float) lenthMax[g] * 5) / somaTotal))
                            : (((float) ((float) lenthMax[g] * 100) / somaTotal) > 20) ? (((float) ((float) lenthMax[g] * 15) / somaTotal))
                                    : (((float) ((float) lenthMax[g] * 100) / somaTotal) > 10) ? (((float) ((float) lenthMax[g] * 25) / somaTotal))
                                            : (((float) ((float) lenthMax[g] * 100) / somaTotal) > 3) ? ((float) ((float) lenthMax[g] * 50) / somaTotal)
                                                    : (((float) ((float) lenthMax[g] * 100) / somaTotal) < 1) ? 1 : ((float) ((float) lenthMax[g] * 100) / somaTotal);
        }

        somaTotal = 0;

        return perncetages;
    }

    public static String toString(Object o) {
        return (o == null) ? "" : o.toString();
    }

    public static void main(String[] args) {
//        ResultSet rs = Call.selectFrom("VER_CONTA_CONTABIL WHERE \"TIPO CONTA\"='Conta Banco'", "*");
//        ResultSet rs = Call.callTableFunction("PACK_REPORT2.FUNCT_REPORT_SINISTRO", "*", null,null);
//        String ff =GenericPDF.createDoc( "Ah","mome Doc", "Teste Titile", rs, GenericPDF.OrientacaoPagina.HORIZONTAL, GenericPDF.TypeParam.OCULTAR,"ID");
//        System.err.println(ff);

//        ResultSet rs = Call.callTableFunction("PACK_REPORT.reportContaMoviment", "*", null,null, new ArrayList<>());
//        String ff =GenericExcel.createDoc( "Ah","Contrato", "Contrato Não Pago", rs, GenericExcel.TypeParam.OCULTAR,
//                "ESTADO","ID","TIPO MOVIMENTO","NUMERO BANCARIO","DESCRICAO","TIPO CONTA","CREDITO","REGISTRO","DEBITO","SALDO");
//        String ff =GenericExcel.createDoc( "Ah","Contrato", "Contrato Não Pago", rs, GenericExcel.TypeParam.OCULTAR);
//        System.err.println(ff);
    }

    public static boolean sheachInParam(String key, int i, String... param) {
        for (String string : param) {
            if (string.equals(key)) {
                return (i == 0) ? (true) : (!true);
            }
        }
        return (i == 0) ? (!true) : true;
    }

    public static void CreateCell(Cell c, Row r, Sheet s, CellStyle cs, int colinaI, int colinaF, String valorS, int linhaI, int linhaF) {

        c = r.createCell(linhaI);
        c.setCellStyle(cs);
        c.setCellValue(valorS);
        s.addMergedRegion(new CellRangeAddress(colinaI, colinaF, linhaI, linhaF));
        for (int e = (linhaI + 1); e <= linhaF; e++) {
            c = r.createCell(e);
            c.setCellStyle(cs);
        }
    }

    public static int toInt(float d) {
        DecimalFormat df = new DecimalFormat("#");
        String s = df.format(d);
        return Integer.parseInt(s);
    }
    
    private static ArrayList<Object[]>removeParamm(ArrayList<Object[]> al)
    {
        boolean b;
        Object[] newList;
        ArrayList<Object[]> newArrayList = new ArrayList<>();
        for (Object[] listActal : al) {
            int h = 0;
            newList = new Object[al.get(0).length-removeItem.length]; 
            for (int ii = 0; ii < listActal.length; ii++) {
                b =true;
                for (int j = 0; j < removeItem.length; j++) {
                   if( ii == removeItem[j].map )
                   { b = false; break;}
                }
                if(b) 
                { newList[h]=listActal[ii]; h++; }
            }
            newArrayList.add(newList);
        }
        return newArrayList;
    }
    
    private static ArrayList<Object[]>showParamm(ArrayList<Object[]> al)
    {
        boolean b;
        Object[] newList;
        ArrayList<Object[]> newArrayList = new ArrayList<>();
        for (Object[] listActal : al) {
            int h = 0;
            newList = new Object[showItem.length]; 
            for (int ii = 0; ii < listActal.length; ii++) {
                b =false;
                for (int j = 0; j < showItem.length; j++) {
                   if( ii == showItem[j].map )
                   { b = true; break;}
                }
                if(b) 
                { newList[h]=listActal[ii]; h++; }
            }
            newArrayList.add(newList);
        }
        return newArrayList;
    }
    
    public static enum Alignment {
        LEFT(-1), CENTER(0), RIGHT(1);
        public final int i;
        Alignment(int i) { this.i = i; } 
    }
    
    private static void calTotalValores(ArrayList<Object[]> list)
    {
       Double valor;
       valoresTotal = new String[list.get(0).length];
        for (Object[] value : list) {
            for (int j = 0; j < value.length; j++) {
                for (int s = 0; s < arrValoresTotal.length; s++) {
                    if( j == arrValoresTotal[s])
                    {   System.err.println(value[j]);
                        valor = ((valoresTotal[j] == null)  ? 0.0 : unFormat(valoresTotal[j]));
                        valor += unFormat(toString(value[j]));
                        valoresTotal[j] = Double.toString(valor);
                    }
                }
            }
        }
        
        if( !Arrays.toString(arrValoresTotal).contains("0") )
            valoresTotal[0] = "TOTAL";
        
        list.add(valoresTotal);
        
        System.err.println(Arrays.toString(valoresTotal));
    }
    
    public static double unFormat(String value) {
        try { return  NumberFormat.getInstance(Locale.FRENCH).parse(value).doubleValue(); } 
        catch (ParseException ex) { return -1;}
    }
}
