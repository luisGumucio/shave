package com.manaco.org.repositories;

import com.manaco.org.dto.TransactionDetailDto;
import com.manaco.org.model.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Integer> {


//    select m.name, m.id, d.item_item_id from transaction_detail d join ware_house_mp m on d.ware_house_id = m.id group by m.id, m.name, d.item_item_id

//    @Query(value = "SELECT new TransactionDetailDto(m.id, m.name, d.item.itemId) FROM TransactionDetail d join WareHouseMp m on d.wareHouseId = m.id GROUP BY m.id, m.name, d.item.itemId" )
//    List<TransactionDetailDto> fetchTransactionDetailDtoJoin();
}
