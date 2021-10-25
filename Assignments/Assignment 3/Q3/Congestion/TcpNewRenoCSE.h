#ifndef TCPNEWRENOCSE_H
#define TCPNEWRENOCSE_H

#include "tcp-congestion-ops.h"

namespace ns3
{
	class TcpNewRenoCSE : public TcpNewReno
	{
	public:
		/**
		 * \brief Get the type ID.
		 * \return the object TypeId
		 */
		static TypeId GetTypeId(void);
		std::string GetName() const;

	protected:
		virtual uint32_t SlowStart(Ptr<TcpSocketState> tcb, uint32_t segmentsAcked);
		virtual void CongestionAvoidance(Ptr<TcpSocketState> tcb, uint32_t segmentsAcked);
	};
}

#endif
