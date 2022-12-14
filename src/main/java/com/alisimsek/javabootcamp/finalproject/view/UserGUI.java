package com.alisimsek.javabootcamp.finalproject.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import com.alisimsek.javabootcamp.finalproject.helper.Helper;
import com.alisimsek.javabootcamp.finalproject.model.CarPolicy;
import com.alisimsek.javabootcamp.finalproject.model.Users;
import com.alisimsek.javabootcamp.finalproject.service.*;
import com.toedter.calendar.JDateChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;

public class UserGUI extends JFrame {

	private JPanel wrapper;
	private Users user;
	private JTextField fld_age_min;
	private JTextField fld_age_max;

	private JTable tbl_carPolicy_list;
	DefaultTableModel mdl_carPolicy_list;
    private Object[] row_carPolicy_list;

	String revenueByMonthlyQuery;
	String pieChartByAgencyQuery;
	String barChartQuery;
	private CarPolicyService carPolicyService = new CarPolicyService();
	private DrawChart drawChart = new DrawChart();
	private CreateQuery createQuery = new CreateQuery();
	private CustomerService customerService = new CustomerService();
	private InsuranceAgencyService insuranceAgencyService = new InsuranceAgencyService();

	public UserGUI(Users user) {

		this.user = user;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 600);
		setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
		setTitle("Anadolubank Java Bootcamp Bitirme Projesi");
		wrapper = new JPanel();
		wrapper.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(wrapper);
		setVisible(true);
		setResizable(false);
		wrapper.setLayout(null);
		JLabel lbl_welcome = new JLabel("user ekran??");
		lbl_welcome.setBounds(5, 5, 1174, 14);
		wrapper.add(lbl_welcome);
		
		lbl_welcome.setText("Ho??geldin " + user.getName());
		
		JPanel pnl_sh_form = new JPanel();
		pnl_sh_form.setBounds(5, 60, 165, 480);
		wrapper.add(pnl_sh_form);
		pnl_sh_form.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Ba??lang???? Tarihi");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 13));
		lblNewLabel.setBounds(10, 20, 100, 20);
		pnl_sh_form.add(lblNewLabel);
	
		JLabel lblBitiTarihi = new JLabel("Biti?? Tarihi");
		lblBitiTarihi.setFont(new Font("Arial", Font.PLAIN, 13));
		lblBitiTarihi.setBounds(10, 70, 100, 20);
		pnl_sh_form.add(lblBitiTarihi);
		
		JLabel lblBitiTarihi_1 = new JLabel("Acenta");
		lblBitiTarihi_1.setFont(new Font("Arial", Font.PLAIN, 13));
		lblBitiTarihi_1.setBounds(10, 120, 100, 20);
		pnl_sh_form.add(lblBitiTarihi_1);

		JComboBox cmb_agency_name = new JComboBox();
		cmb_agency_name.setBounds(10, 140, 150, 20);
		pnl_sh_form.add(cmb_agency_name);
		
		JLabel lblBitiTarihi_1_1 = new JLabel("??ehir");
		lblBitiTarihi_1_1.setFont(new Font("Arial", Font.PLAIN, 13));
		lblBitiTarihi_1_1.setBounds(10, 170, 100, 20);
		pnl_sh_form.add(lblBitiTarihi_1_1);

		JComboBox cmb_city = new JComboBox();
		cmb_city.setBounds(10, 190, 150, 20);
		pnl_sh_form.add(cmb_city);

		JLabel lblBitiTarihi_1_1_1 = new JLabel("Ya?? Aral??????");
		lblBitiTarihi_1_1_1.setFont(new Font("Arial", Font.PLAIN, 13));
		lblBitiTarihi_1_1_1.setBounds(10, 220, 100, 20);
		pnl_sh_form.add(lblBitiTarihi_1_1_1);
		
		fld_age_min = new JTextField();
		fld_age_min.setBounds(10, 240, 40, 20);
		pnl_sh_form.add(fld_age_min);
		fld_age_min.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("-");
		lblNewLabel_1.setBounds(58, 240, 22, 20);
		pnl_sh_form.add(lblNewLabel_1);
		
		fld_age_max = new JTextField();
		fld_age_max.setColumns(10);
		fld_age_max.setBounds(70, 240, 40, 20);
		pnl_sh_form.add(fld_age_max);
		
		JDateChooser dateChooser_start = new JDateChooser();
		dateChooser_start.setBounds(10, 40, 150, 20);
		pnl_sh_form.add(dateChooser_start);
		
		JDateChooser dateChooser_end = new JDateChooser();
		dateChooser_end.setBounds(10, 90, 150, 20);
		pnl_sh_form.add(dateChooser_end);
		
		JTabbedPane tab_wrapper = new JTabbedPane(JTabbedPane.TOP);
		tab_wrapper.setBounds(180, 60, 975, 480);
		wrapper.add(tab_wrapper);
		
		JPanel pnl_carPolicy_list = new JPanel();
		tab_wrapper.addTab("Sonu?? Tablosu", null, pnl_carPolicy_list, null);
		pnl_carPolicy_list.setLayout(null);
		
		JScrollPane scrl_carPolicy_list = new JScrollPane();
		scrl_carPolicy_list.setBounds(0, 0, 970, 454);
		pnl_carPolicy_list.add(scrl_carPolicy_list);
		
		tbl_carPolicy_list = new JTable();
		scrl_carPolicy_list.setViewportView(tbl_carPolicy_list);

//Acenta isimlerinin filtreleme combo box'??na aktar??lmas??
		List<String> agencyNameList = insuranceAgencyService.getAgencyName();
		cmb_agency_name.setModel(new DefaultComboBoxModel<String>(agencyNameList.toArray(new String[0])));

//M????terilerin ??ehirlerinin filtreleme combo box'??na aktar??lmas??
		List<String> customersCityNameList = customerService.getCityName();
		cmb_city.setModel(new DefaultComboBoxModel<String>(customersCityNameList.toArray(new String[0])));

//Ara?? sigortalar?? tablosu kodlar?? ba??lang??c??
        mdl_carPolicy_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 1)
                    return false;
                return super.isCellEditable(row, column);
            }
        };
        
        Object[] col_carInsurance_list = {"id", "??sim Soyisim", "Ya??", "Cinsiyet", "Email", "??ehir", "Ara?? Markas??", "Ara?? Y??l??", 
        		  "Poli??e Ba??lang??c??", "Poli??e Biti??i", "Fiyat", "????lem Tarihi", "Acenta"};
        mdl_carPolicy_list.setColumnIdentifiers(col_carInsurance_list);
        row_carPolicy_list = new Object[col_carInsurance_list.length];
        tbl_carPolicy_list.setModel(mdl_carPolicy_list);
        tbl_carPolicy_list.getTableHeader().setReorderingAllowed(false);
//Ara?? sigortalar?? tablosu kodlar?? biti??i


//rapor ??al????t??r butonu kodlar?? ba??lang??c??
    	JButton btn_searh = new JButton("Raporu ??al????t??r");
		btn_searh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  //tarih verileri i??in format
				String startDate;
				if (dateChooser_start.getDate() == null) {	//tarih bo?? b??rak??l??p rapor ??al????t??r??l??rsa hata almamak i??in
					startDate = "";
				}
				else {
					startDate = formatter.format(dateChooser_start.getDate());
				}
				
				String endDate;
				if (dateChooser_end.getDate() == null) {	//tarih bo?? b??rak??l??p rapor ??al????t??r??l??rsa hata almamak i??in
					endDate = "";
				}
				else {
					endDate = formatter.format(dateChooser_end.getDate());
				}

				String agencyName = cmb_agency_name.getSelectedItem().toString();
				String city=cmb_city.getSelectedItem().toString();
				String ageMin=fld_age_min.getText().trim();
				String ageMax=fld_age_max.getText().trim();
		
//sorgu algoritmas??
				//filtreleme girdilerine g??re query olu??turulur
				String searchQuery = createQuery.search(startDate, endDate, agencyName, city, ageMin, ageMax );
				if(!searchQuery.equals("")) {

					//filtreleme query si ile databaseden veriler searchCarPolicyList e aktar??l??r
					List<CarPolicy> searchCarPolicyList = carPolicyService.searchList (searchQuery);

					if(searchCarPolicyList.size() < 1) {
						Helper.showMsg("Aranan kriterlere uygun sonu?? bulunamad??");
					}

					loadCarPolicyModel(searchCarPolicyList);  //arama sonu??lar?? tablo olarak kullan??c??ya g??sterilir
				}
				//filtreleme girdilerine g??re ayl??k kazan?? grafi??i i??in query olu??turulur
				revenueByMonthlyQuery = createQuery.revenueByMonthly(startDate, endDate, agencyName, city, ageMin, ageMax); //filtreleme sonucuna g??re ??izgi grafik data seti

				//filtreleme girdilerine g??re gelirdeki acenta oranlar??n??n grafi??i i??in query olu??turulur
				pieChartByAgencyQuery = createQuery.pieChartByAgency(startDate, endDate, agencyName, city, ageMin, ageMax);  //filtreleme sonucuna g??re pasta grafik data seti

				//filtreleme girdilerine g??re ayl??k m????teri i??lem say??s?? grafi??i i??in query olu??turulur
				barChartQuery = createQuery.barChart(startDate, endDate, agencyName, city, ageMin, ageMax);
			}
		});
		btn_searh.setBounds(22, 300, 120, 25);
		pnl_sh_form.add(btn_searh);

		//Kullan??c?? ekran??ndaki ayl??k kazan?? grafi??i olu??turma kodlar??
		JCheckBox check_box = new JCheckBox("Ayl??k gelir grafi??i");
		check_box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if(check_box.isSelected()) {		
					drawChart.lineChartRevenueByMonth(revenueByMonthlyQuery);
					check_box.setSelected(false);
				}
			}
		});
		check_box.setBounds(180, 30, 140, 23);
		wrapper.add(check_box);

		//Kullan??c?? ekran??ndaki acenta bazl?? gelir grafi??i olu??turma kodlar??
		JCheckBox chckbox_piechart = new JCheckBox("Acenta Gelir Da????l??m?? Grafi??i");
		chckbox_piechart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbox_piechart.isSelected()) {
					drawChart.pieChartAgency(pieChartByAgencyQuery);
					chckbox_piechart.setSelected(false);
				}
			}
		});
		chckbox_piechart.setBounds(320, 30, 190, 23);
		wrapper.add(chckbox_piechart);

		//Ay bazl?? i??lem yapan m????teri say??s?? grafi??i olu??turma kodlar??
		JCheckBox chckbox_barchart = new JCheckBox("Ay Bazl?? ????lem Yapan M????teri Grafi??i");
		chckbox_barchart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbox_barchart.isSelected()) {
					drawChart.barCartByCustomer(barChartQuery);
					chckbox_barchart.setSelected(false);
				}
			}
		});
		chckbox_barchart.setBounds(520, 30, 240, 23);
		wrapper.add(chckbox_barchart);
	}

	private void loadCarPolicyModel(List<CarPolicy> list) {
		DefaultTableModel clearModel = (DefaultTableModel) tbl_carPolicy_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (CarPolicy obj : list){
            i = 0;
            row_carPolicy_list[i++] = obj.getId();
			row_carPolicy_list[i++] = obj. getCustomer().getFullName();
            row_carPolicy_list[i++] = obj.getCustomer().getAge();
            row_carPolicy_list[i++] = obj.getCustomer().getGender();
            row_carPolicy_list[i++] = obj.getCustomer().getEmail();
            row_carPolicy_list[i++] = obj.getCustomer().getCity();
            row_carPolicy_list[i++] = obj.getCarMake();
            row_carPolicy_list[i++] = obj.getCarYear();
            row_carPolicy_list[i++] = obj.getEffectiveDate();
            row_carPolicy_list[i++] = obj.getExpirationDate();
            row_carPolicy_list[i++] = obj.getPrice();
            row_carPolicy_list[i++] = obj.getTransactionDate(); 
            row_carPolicy_list[i++] = obj.getInsuranceAgency().getName();
            mdl_carPolicy_list.addRow(row_carPolicy_list);
        }
	}
}
