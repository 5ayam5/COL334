#include "TcpNewRenoCSE.h"
#include "ns3/log.h"

using namespace ns3;

NS_OBJECT_ENSURE_REGISTERED(TcpNewRenoCSE);

uint32_t TcpNewRenoCSE::SlowStart(Ptr<TcpSocketState> tcb, uint32_t segmentsAcked)
{
	NS_LOG_FUNCTION(this << tcb << segmentsAcked);

	if (segmentsAcked >= 1)
	{
		tcb->m_cWnd += static_cast<uint32_t>(ceil(pow(static_cast<double>(tcb->m_segmentSize), 1.9) / tcb->m_cWnd.Get()));
		NS_LOG_INFO("In SlowStart, updated to cwnd " << tcb->m_cWnd << " ssthresh " << tcb->m_ssThresh);
		return segmentsAcked - 1;
	}

	return 0;
}

void TcpNewRenoCSE::CongestionAvoidance(Ptr<TcpSocketState> tcb, uint32_t segmentsAcked)
{
	NS_LOG_FUNCTION(this << tcb << segmentsAcked);

	if (segmentsAcked >= 1)
	{
		tcb->m_cWnd += (tcb->m_segmentSize + 1) / 2;
		NS_LOG_INFO("In SlowStart, updated to cwnd " << tcb->m_cWnd << " ssthresh " << tcb->m_ssThresh);
	}
}

TypeId TcpNewRenoCSE::GetTypeId(void)
{
	static TypeId tid = TypeId("ns3::TcpNewRenoCSE")
							.SetParent<TcpNewReno>()
							.SetGroupName("Internet")
							.AddConstructor<TcpNewRenoCSE>();
	return tid;
}

std::string TcpNewRenoCSE::GetName() const
{
	return "TcpNewRenoCSE";
}
