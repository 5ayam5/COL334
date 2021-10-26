/* -*- Mode:C++; c-file-style:"gnu"; indent-tabs-mode:nil; -*- */
/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation;
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

#include <fstream>
#include "ns3/core-module.h"
#include "ns3/network-module.h"
#include "ns3/internet-module.h"
#include "ns3/point-to-point-module.h"
#include "ns3/applications-module.h"

using namespace ns3;

NS_LOG_COMPONENT_DEFINE("Q3_Script");

class MyApp : public Application
{
public:
	MyApp();
	virtual ~MyApp();

	/**
   * Register this type.
   * \return The TypeId.
   */
	static TypeId GetTypeId(void);
	void Setup(Ptr<Socket> socket, Address address, uint32_t packetSize, uint32_t nPackets, DataRate dataRate);

private:
	virtual void StartApplication(void);
	virtual void StopApplication(void);

	void ScheduleTx(void);
	void SendPacket(void);

	Ptr<Socket> m_socket;
	Address m_peer;
	uint32_t m_packetSize;
	uint32_t m_nPackets;
	DataRate m_dataRate;
	EventId m_sendEvent;
	bool m_running;
	uint32_t m_packetsSent;
};

MyApp::MyApp()
	: m_socket(0),
	  m_peer(),
	  m_packetSize(0),
	  m_nPackets(0),
	  m_dataRate(0),
	  m_sendEvent(),
	  m_running(false),
	  m_packetsSent(0)
{
}

MyApp::~MyApp()
{
	m_socket = 0;
}

/* static */
TypeId MyApp::GetTypeId(void)
{
	static TypeId tid = TypeId("MyApp")
							.SetParent<Application>()
							.SetGroupName("Q3")
							.AddConstructor<MyApp>();
	return tid;
}

void MyApp::Setup(Ptr<Socket> socket, Address address, uint32_t packetSize, uint32_t nPackets, DataRate dataRate)
{
	m_socket = socket;
	m_peer = address;
	m_packetSize = packetSize;
	m_nPackets = nPackets;
	m_dataRate = dataRate;
}

void MyApp::StartApplication(void)
{
	m_running = true;
	m_packetsSent = 0;
	m_socket->Bind();
	m_socket->Connect(m_peer);
	SendPacket();
}

void MyApp::StopApplication(void)
{
	m_running = false;

	if (m_sendEvent.IsRunning())
	{
		Simulator::Cancel(m_sendEvent);
	}

	if (m_socket)
	{
		m_socket->Close();
	}
}

void MyApp::SendPacket(void)
{
	Ptr<Packet> packet = Create<Packet>(m_packetSize);
	m_socket->Send(packet);

	if (++m_packetsSent < m_nPackets)
	{
		ScheduleTx();
	}
}

void MyApp::ScheduleTx(void)
{
	if (m_running)
	{
		Time tNext(Seconds(m_packetSize * 8 / static_cast<double>(m_dataRate.GetBitRate())));
		m_sendEvent = Simulator::Schedule(tNext, &MyApp::SendPacket, this);
	}
}

static void
CwndChange(Ptr<OutputStreamWrapper> stream, uint32_t oldCwnd, uint32_t newCwnd)
{
	// NS_LOG_UNCOND(Simulator::Now().GetSeconds() << "\t" << newCwnd);
	*stream->GetStream() << Simulator::Now().GetSeconds() << "\t" << oldCwnd << "\t" << newCwnd << '\n';
}

static void
RxDrop(Ptr<OutputStreamWrapper> stream, Ptr<const Packet> p)
{
	// NS_LOG_UNCOND("RxDrop at " << Simulator::Now().GetSeconds());
	*stream->GetStream() << Simulator::Now().GetSeconds() << '\n';
}

void setP2PHelper(PointToPointHelper &helper, int data_rate, int delay)
{
	helper.SetDeviceAttribute("DataRate", StringValue(std::to_string(data_rate) + "Mbps"));
	helper.SetChannelAttribute("Delay", StringValue(std::to_string(delay) + "ms"));
}

PacketSinkHelper getPacketSinkHelper(uint16_t sinkPort)
{
	return PacketSinkHelper("ns3::TcpSocketFactory", InetSocketAddress(Ipv4Address::GetAny(), sinkPort));
}

int main(int argc, char *argv[])
{
	TypeId newRenoCSE = TypeId::LookupByName("ns3::TcpNewRenoCSE");
	int config = 1;

	CommandLine cmd;
	cmd.AddValue("config", "Configuration", config);
	cmd.Parse(argc, argv);

	NodeContainer nodes;
	nodes.Create(3);

	PointToPointHelper pointToPoint[2];
	setP2PHelper(pointToPoint[0], 10, 3);
	setP2PHelper(pointToPoint[1], 9, 3);

	NetDeviceContainer devices[2];
	devices[0] = pointToPoint[0].Install(nodes.Get(0), nodes.Get(2));
	devices[1] = pointToPoint[1].Install(nodes.Get(1), nodes.Get(2));

	Ptr<RateErrorModel> em[2] = { CreateObject<RateErrorModel>(), CreateObject<RateErrorModel>()};
	for (int i = 0; i < 2; ++i)
		em[i]->SetAttribute("ErrorRate", DoubleValue(0.00001));
	for (int i = 0; i < 2; ++i)
		devices[i].Get(1)->SetAttribute("ReceiveErrorModel", PointerValue(em[i]));

	InternetStackHelper stack;
	stack.Install(nodes);

	Ipv4AddressHelper address[2];
	address[0].SetBase("10.1.1.0", "255.255.255.252");
	address[1].SetBase("10.1.2.0", "255.255.255.252");
	Ipv4InterfaceContainer interfaces[2] = {address[0].Assign(devices[0]), address[1].Assign(devices[1])};

	uint16_t sinkPorts[3] = {8080, 8081, 8082};
	Address sinkAddress[3] = {Address(InetSocketAddress(interfaces[0].GetAddress(1), sinkPorts[0])), Address(InetSocketAddress(interfaces[0].GetAddress(1), sinkPorts[1])), Address(InetSocketAddress(interfaces[1].GetAddress(1), sinkPorts[2]))};
	PacketSinkHelper packetSinkHelpers[3] = {getPacketSinkHelper(sinkPorts[0]), getPacketSinkHelper(sinkPorts[1]), getPacketSinkHelper(sinkPorts[2])};
	ApplicationContainer sinkApps[3] = {packetSinkHelpers[0].Install(nodes.Get(2)), packetSinkHelpers[1].Install(nodes.Get(2)), packetSinkHelpers[2].Install(nodes.Get(2))};
	for (int i = 0; i < 3; ++i)
	{
		sinkApps[i].Start(Seconds(0.));
		sinkApps[i].Stop(Seconds(30.));
	}

	if (config == 2)
	{
		Config::Set("/NodeList/" + std::to_string(nodes.Get(1)->GetId()) + "/$ns3::TcpL4Protocol/SocketType", TypeIdValue(newRenoCSE));
	}
	else if (config == 3)
		Config::Set("/NodeList/*/$ns3::TcpL4Protocol/SocketType", TypeIdValue(newRenoCSE));
	Ptr<Socket> ns3TcpSocket[3] = {Socket::CreateSocket(nodes.Get(0), TcpSocketFactory::GetTypeId()), Socket::CreateSocket(nodes.Get(0), TcpSocketFactory::GetTypeId()), Socket::CreateSocket(nodes.Get(1), TcpSocketFactory::GetTypeId())};

	Ptr<MyApp> app[3] = {CreateObject<MyApp>(), CreateObject<MyApp>(), CreateObject<MyApp>()};
	for (int i = 0; i < 3; ++i)
		app[i]->Setup(ns3TcpSocket[i], sinkAddress[i], 3000, 1000000000, DataRate("1.5Mbps"));
	nodes.Get(0)->AddApplication(app[0]);
	nodes.Get(0)->AddApplication(app[1]);
	nodes.Get(1)->AddApplication(app[2]);
	app[0]->SetStartTime(Seconds(1.));
	app[0]->SetStopTime(Seconds(20.));
	app[1]->SetStartTime(Seconds(5.));
	app[1]->SetStopTime(Seconds(25.));
	app[2]->SetStartTime(Seconds(15.));
	app[2]->SetStopTime(Seconds(30.));

	AsciiTraceHelper asciiTraceHelper;
	Ptr<OutputStreamWrapper> stream[3] = {asciiTraceHelper.CreateFileStream("outputs/config" + std::to_string(config) + "connection1.cwnd"), asciiTraceHelper.CreateFileStream("outputs/config" + std::to_string(config) + "connection2.cwnd"), asciiTraceHelper.CreateFileStream("outputs/config" + std::to_string(config) + "connection3.cwnd")};
	for (int i = 0; i < 3; ++i)
		ns3TcpSocket[i]->TraceConnectWithoutContext("CongestionWindow", MakeBoundCallback(&CwndChange, stream[i]));

	Ptr<OutputStreamWrapper> drop_stream = asciiTraceHelper.CreateFileStream("outputs/config" + std::to_string(config) + ".drop");
	devices[0].Get(1)->TraceConnectWithoutContext("PhyRxDrop", MakeBoundCallback(&RxDrop, drop_stream));
	devices[1].Get(1)->TraceConnectWithoutContext("PhyRxDrop", MakeBoundCallback(&RxDrop, drop_stream));

	Simulator::Stop(Seconds(30));
	Simulator::Run();
	Simulator::Destroy();

	return 0;
}
