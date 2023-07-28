package com.example.mobco.Repository

import android.app.Activity
import com.example.mobco.Model.ApiRequestBody.InspectMaterialBody
import com.example.mobco.Model.ApiRequestBody.ItemsReceivingBody
import com.example.mobco.Model.ApiRequestBody.PoLine
import com.example.mobco.Model.ApiRequestBody.PutawayMaterialBody
import com.example.mobco.Ui.SignInFragment.Companion.EMPLOYEE_ID
import com.example.mobco.Ui.SignInFragment.Companion.USER_ID

class ReceivingRepository(activity: Activity?) :
    BaseRepository(activity) {
    private val TRANSACTION_DATE="24-JUL-11"
    private val PROGRAM_ID = 32766
    private val PROGRAM_APPLICATION_ID = 201

    suspend fun getPurchaseOrdersList(orgNum:String, poNum:String) = apiInterface.getPurchaseOrdersList(userId,deviceSerialNo,lang,orgNum,poNum)
        suspend fun getPurchaseOrderDetailsList(orgNum:String,poNum:String) = apiInterface.getPurchaseOrderDetailsList(userId,deviceSerialNo,lang,orgNum,poNum)
        suspend fun ItemReceiving(poHeaderId: Int,
                                  poLines :List<PoLine>,
                                  poOrgId:String,
                                    trasnactionDate: String) =
            apiInterface.ItemReceiving(
                ItemsReceivingBody(
                    employee_id = EMPLOYEE_ID,
                    user_id = USER_ID,
                    po_header_id = poHeaderId,
                    po_lines = poLines,
                    program_id = PROGRAM_ID,
                    program_application_id = PROGRAM_APPLICATION_ID,
                    transaction_date = trasnactionDate,
                    po_org_id = poOrgId
            )
            )

    suspend fun getPurchaseOrderReceiptNoList(orgNum:String,poNum:String,receiptNo:String) = apiInterface.getPurchaseOrderReceiptNoList(orgNum,poNum, receiptNo)
    suspend fun InspectMaterial(poHeaderId: Int,
                              poLineId :Int,
                              acceptedQty :Int,
                              receiptNo: String,
                                shipToOrganizationId:Int,
                            transactionDate: String) =
        apiInterface.InspectMaterial(
            InspectMaterialBody(
                employee_id = EMPLOYEE_ID,
                user_id = USER_ID,
                po_header_id = poHeaderId,
                po_line_id = poLineId,
                itemqtyaccepted = acceptedQty,
                receiptno = receiptNo,
                ship_to_organization_id = shipToOrganizationId,
                transaction_date = transactionDate
            )
        )
    suspend fun PutAwayMaterial(poHeaderId: Int,
                                poLineId :Int,
                                locator_id :String,
                                subinventory_code: String,
                                receiptNo: String,
                                shipToOrganizationId:Int,
                                transactionDate:String) =
        apiInterface.PutawayMaterial(
            PutawayMaterialBody(
                employee_id = EMPLOYEE_ID,
                user_id = USER_ID,
                po_header_id = poHeaderId,
                po_line_id = poLineId,
                locator_id = locator_id,
                subinventory_code = subinventory_code,
                receiptno = receiptNo,
                ship_to_organization_id = shipToOrganizationId,
                transaction_date = transactionDate
            )
        )
}