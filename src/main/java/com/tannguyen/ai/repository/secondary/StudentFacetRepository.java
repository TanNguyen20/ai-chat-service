package com.tannguyen.ai.repository.secondary;

import java.util.List;

public interface StudentFacetRepository {
    List<String> distinctCoSo();
    List<String> distinctKhoa();
    List<String> distinctNganh();
    List<String> distinctGioiTinh();
    List<String> distinctBacDaoTao();
    List<String> distinctLoaiHinhDaoTao();
}