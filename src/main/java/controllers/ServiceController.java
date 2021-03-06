package controllers;

import java.io.*;
import java.util.*;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import entities.Service;
import ejb.ServiceEJB;
import com.pdfjet.*;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;

/**
 *
 * A request scoped controller to handle all the operation related to service entity
 */
@Named(value = "serviceController")
@RequestScoped
public class ServiceController {

    /**
     * Default constructor
     */
    public ServiceController() {
    }
    //EJB injection
    @EJB
    private ServiceEJB serviceEJB;
    private String serviceCategory;

    private Service service = new Service();
    private List<Service> serviceList = new ArrayList<>();
    private List<Service> serviceListLimitFour = new ArrayList<>();
    private List<Service> frontendServiceList = new ArrayList<>();

    private String search = "";
    private String searchBy = "";

    /**
     * Function to return list of services  to be displayed in the frontend
     * @return list of services
     */
    public List<Service> getFrontendServiceList() {
        if (this.search.isEmpty()) {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String search_by = params.get("search_by");
            String search_text = params.get("s");
            if (search_by != null && search_text != null) {
                frontendServiceList = serviceEJB.searchFrontend(search_text, search_by);
                FacesMessage infoMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Search result for: " + search, "");
                FacesContext.getCurrentInstance().addMessage(null, infoMsg);
            } else {
                frontendServiceList = serviceEJB.listFrontendService();
            }
        }

        return frontendServiceList;
    }

    /**
     *
     * @param frontendServiceList to set the attribute frontendServiceList
     */
    public void setFrontendServiceList(List<Service> frontendServiceList) {
        this.frontendServiceList = frontendServiceList;
    }

    /**
     *
     * @return list of service with limit of four
     */
    public List<Service> getServiceListLimitFour() {
        serviceListLimitFour = serviceEJB.listServiceLimitFour();
        return serviceListLimitFour;
    }

    /**
     *
     * @param serviceListLimitFour to set the attribute serviceListLimitFour
     */
    public void setServiceListLimitFour(List<Service> serviceListLimitFour) {
        this.serviceListLimitFour = serviceListLimitFour;
    }

    /**
     * Function to create service object and persist it in the database
     * @return string: name of the page to be rendered
     */
    public String doCreateService() {
        service = serviceEJB.addService(service, serviceCategory);
        FacesMessage infoMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Service Created Succefully.", "");
        FacesContext.getCurrentInstance().addMessage(null, infoMsg);
        return "listService.xhtml?faces-redirect=true";
    }

    /**
     * Functio to delete a service object
     * @param id to be passed to find a service object
     * @return string: name of the page to be rendered
     */
    public String doDeleteService(Long id) {
        serviceEJB.deleteService(id);
        return "listService.xhtml?faces-redirect=true";
    }

    /**
     * Function to perform search operation on service objects
     * @return string: name of the page to be rendered
     */
    public String doSearch() {
        serviceList = serviceEJB.search(search, searchBy);
        FacesMessage infoMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Search result for: " + search, "");
        FacesContext.getCurrentInstance().addMessage(null, infoMsg);

        return "listService.xhtml";

    }

    /**
     * Function to perform search on services from frontend
     * @return
     */
    public String doFrontEndSearch() {
        return "frontendListServices.xhtml?faces-redirect=true&search_by=" + searchBy + "&s=" + search;
    }

    /**
     *
     * @return
     */
    public String getSearchBy() {
        return searchBy;
    }

    /**
     *
     * @param searchBy
     */
    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    /**
     *
     * @return
     */
    public String getSearch() {
        return search;
    }

    /**
     *
     * @param search
     */
    public void setSearch(String search) {
        this.search = search;
    }

    /**
     *
     * @return
     */
    public Service getServiceByParamId() {
        service = serviceEJB.getServiceByParamId();
        return service;
    }

    /**
     *
     * @return
     */
    public List<Service> getServiceList() {
        if (this.search.isEmpty()) {
            serviceList = serviceEJB.listService();
        }
        return serviceList;
    }

    /**
     *
     * @param serviceList
     */
    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    /**
     *
     * @return
     */
    public Service getService() {
        return service;
    }

    /**
     *
     * @param service
     */
    public void setService(Service service) {
        this.service = service;
    }

    /**
     *
     * @return
     */
    public String getServiceCategory() {
        return serviceCategory;
    }

    /**
     *
     * @param serviceCategory
     */
    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    /**
     * functio to create a invoice as per the service order
     * @throws Exception
     */
    public void createPdf() throws Exception {
        String machine_name = InetAddress.getLocalHost().getHostName();
        String path_to_desktop = "C:/Documents and Settings/" + machine_name + "/Desktop/";
        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream(path_to_desktop + "invoice.pdf")));
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        Date dateobj = new Date();
        Page page = new Page(pdf, A4.PORTRAIT);

        Font f1 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        f1.setSize(7f);

        Font f2 = new Font(pdf, CoreFont.HELVETICA);
        f2.setSize(7f);

        Font f3 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        f3.setSize(20f);

        Font f4 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        f4.setSize(10f);

        TextLine text = new TextLine(f4,
                "Easy Services");
        text.setPosition(90, 60);
        // text.setColor(Color.dodgerblue);
        text.drawOn(page);

        TextLine text2 = new TextLine(f2,
                "400, Kent Street 2230");
        text2.setPosition(90, 70);
        //text2.setColor(Color.dodgerblue);
        text2.drawOn(page);

        TextLine text3 = new TextLine(f2,
                "Phone No:30596785");
        text3.setPosition(90, 80);
        //text3.setColor(Color.dodgerblue);
        text3.drawOn(page);

        TextLine text4 = new TextLine(f2,
                "Fax:456457");
        text4.setPosition(90, 90);
        //text4.setColor(Color.dodgerblue);
        text4.drawOn(page);

        TextLine text8 = new TextLine(f3,
                "INVOICE");
        text8.setPosition(280, 30);
        text8.setColor(Color.dodgerblue);
        text8.drawOn(page);

        TextLine text9 = new TextLine(f4,
                "Bill To");
        text9.setPosition(90, 110);
        //text9.setColor(Color.dodgerblue);
        text9.drawOn(page);

        TextLine payable = new TextLine(f2,
                "Notes:");
        payable.setPosition(90, 320);
        //payable.setColor(Color.dodgerblue);
        payable.drawOn(page);

        TextLine allpay = new TextLine(f2,
                "1. Make all amount payable to Easy Services");
        allpay.setPosition(90, 330);
        //allpay.setColor(Color.dodgerblue);
        allpay.drawOn(page);

        TextLine totaldue = new TextLine(f2,
                "2. Total amount due in 30 days");
        totaldue.setPosition(90, 340);
        //totaldue.setColor(Color.dodgerblue);
        totaldue.drawOn(page);

        TextLine include = new TextLine(f2,
                "3. Include your invoice number on the cheque");
        include.setPosition(90, 350);
        // include.setColor(Color.dodgerblue);
        include.drawOn(page);

        TextLine contact = new TextLine(f2,
                "If you have any question about this invoice please email us at: info@easyservice.com");
        contact.setPosition(180, 400);
        // contact.setColor(Color.dodgerblue);
        contact.drawOn(page);

        TextLine thankyou = new TextLine(f1,
                "THANK YOU FOR YOUR BUSINESS!!");
        thankyou.setPosition(250, 420);
        //thankyou.setColor(Color.dodgerblue);
        thankyou.drawOn(page);

        TextLine copyright = new TextLine(f2,
                "Copyright EasyService 2016");
        copyright.setPosition(265, 430);
        //copyright.setColor(Color.dodgerblue);
        copyright.drawOn(page);

        // dynamic section
        // dynamic variables
        TextLine text5 = new TextLine(f2,
                "Date:" + dateobj);
        text5.setPosition(450, 60);
        //text5.setColor(Color.dodgerblue);
        text5.drawOn(page);

        TextLine text6 = new TextLine(f2,
                "INVOICE NO:3909");
        text6.setPosition(450, 70);
        //text6.setColor(Color.dodgerblue);
        text6.drawOn(page);

        TextLine text7 = new TextLine(f2,
                "CUSTOMER NO:3909");
        text7.setPosition(450, 80);
        //text7.setColor(Color.dodgerblue);
        text7.drawOn(page);

        TextLine text10 = new TextLine(f2,
                "Customer Name");
        text10.setPosition(90, 120);
        //text10.setColor(Color.dodgerblue);
        text10.drawOn(page);

        TextLine text11 = new TextLine(f2,
                "Phone No:30596785");
        text11.setPosition(90, 130);
        //text11.setColor(Color.dodgerblue);
        text11.drawOn(page);

        TextLine text12 = new TextLine(f2,
                "Address: Address1");
        text12.setPosition(90, 140);
        //text12.setColor(Color.dodgerblue);
        text12.drawOn(page);

        List<List<Cell>> tableData = new ArrayList<List<Cell>>();

        List<Cell> row = new ArrayList<Cell>();
        Cell cell = new Cell(f1, "Description");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f1, "Rate");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f1, "Total Hour");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f1, "Total Amount");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        tableData.add(row);

        row = new ArrayList<Cell>();
        cell = new Cell(f2, "Service Name1:");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f2, "20");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f2, "20");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f2, "400");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);
        tableData.add(row);

        row = new ArrayList<Cell>();
        cell = new Cell(f2, "Service Name2");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f2, "20");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f2, "20");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f2, "400");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);
        tableData.add(row);

        row = new ArrayList<Cell>();
        cell = new Cell(f2, "");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f2, "");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f2, "");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f2, "");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);
        tableData.add(row);

        row = new ArrayList<Cell>();
        cell = new Cell(f2, "");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f2, "");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f2, "");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f2, "");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);
        tableData.add(row);

        row = new ArrayList<Cell>();
        cell = new Cell(f2, "");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f2, "");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f2, "");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f2, "");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);
        tableData.add(row);

        TextLine textsubtotal = new TextLine(f1,
                "Sub Total  $800");
        textsubtotal.setPosition(430, 280);
        //textsubtotal.setColor(Color.dodgerblue);
        textsubtotal.drawOn(page);

        TextLine dueline = new TextLine(f1,
                "-------------------------------");
        dueline.setPosition(430, 290);
        //dueline.setColor(Color.dodgerblue);
        dueline.drawOn(page);

        TextLine dueamount = new TextLine(f1,
                "Total Due Amount  $800");
        dueamount.setPosition(430, 300);
        //dueamount.setColor(Color.dodgerblue);
        dueamount.drawOn(page);

        Table table = new Table();
        table.setData(tableData, Table.DATA_HAS_1_HEADER_ROWS);
        table.setLocation(90f, 160f);
        table.setColumnWidth(0, 110f);
        table.setColumnWidth(1, 110f);
        table.setColumnWidth(2, 110f);
        table.setColumnWidth(3, 110f);
        table.wrapAroundCellText();
        // table.wrapAroundCellText2();

        int numOfPages = table.getNumberOfPages(page);
        while (true) {
            table.drawOn(page);
            if (!table.hasMoreData()) {
                break;
            }
            page = new Page(pdf, A4.PORTRAIT);
            table.setLocation(70f, 20f);
        }

        pdf.close();
    }

}
