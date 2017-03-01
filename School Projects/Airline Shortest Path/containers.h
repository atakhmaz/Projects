#ifndef CONTAINERS
#define CONTAINERS

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <ctime>
#include <map>
#include <cmath>
#include <algorithm>
#include <set>
#include <ctype.h>

using std::clock_t;
using std::clock;
using std::ostream;
using std::ofstream;
using std::ifstream;
using std::string;
using std::cout;
using std::endl;
using std::getline;
using std::vector;
using std::map;
using std::stoi;
using std::stod;
using std::set;
using std::sort;

#define AIRPORT_LINE_SIZE 8
#define ROUTES_LINE_SIZE 6
#define AIRLINES_LINE_SIZE 6
#define AIRPORT_DATA_SIZE 7
#define EDGE_DATA_SIZE 9

#define RADIUS_OF_EARTH 3959
#define PI 3.14159265359

enum{
    AIRPORT_ID,
    AIRPORT_NAME,
    AIRPORT_CITY,
    COUNTRY,
    IATA,
    ICAO,
    LATITUDE,
    LONGITUDE
};
enum{
    AIRLINE_CODE,
    AIRLINE_ID,
    SOURCE_AIRPORT_CODE,
    SOURCE_AIRPORT_ID,
    DESTINATION_AIRPORT_CODE,
    DESTINATION_AIRPORT_ID
};
enum {
    AIRLINES_ID,
    AIRLINE_NAME,
    AIRLINE_IATA,
    AIRLINE_ICAO,
    AIRLINE_COUNTRY,
    ACTIVE,
};
enum{
    NEXT_AIRPORT_ID,
    NEXT_AIRPORT_NAME,
    NEXT_AIRPORT_IATA,
    NEXT_AIRPORT_CITY,
    NEXT_AIRPORT_COUNTRY,
    NEXT_AIRPORT_CARRIER,
    NEXT_AIRPORT_CARRIER_NAME,
    NEXT_AIRPORT_DISTANCE,
    NEXT_AIRPORT_CARRIER_ID,
};

struct Routes
{
    Routes(int airlineID, const string& dest_ID, const string& iata, const string& dest_Name, const string& dest_country, const string& dest_city, int nextAp, double dis):
        airline_id(airlineID),
        destination_airport_iata(dest_ID),
        airline_iata(iata),
        destination_airport_name(dest_Name),
        destination_airport_country(dest_country),
        destination_airport_city(dest_city),
        nextAirport(nextAp),
        distance(dis)
    {}
    Routes(const string& carrier, int next, double d):nextAirport(next), distance(d)
    {
        carriers.push_back(carrier);
    }
    void pushNewCarrier(const string& carrier)
    {
        carriers.push_back(carrier);
    }
    friend ostream& operator<<(ostream& out, const Routes& other)
    {
        cout << "Next airport is: " << other.nextAirport << " with a distance of: " << other.distance << " and carriers: /n";
        for(vector<string>::const_iterator it = other.carriers.begin(); it != other.carriers.end(); ++it)
        {
            out << *it << " ";
        }
        return out;
    }
    int airline_id;
    string destination_airport_iata, airline_iata, destination_airport_name, destination_airport_country, destination_airport_city;
    vector<string> carriers;
    int nextAirport;
    double distance;
};

struct Vertex
{
    Vertex(){}
    Vertex(int id, const string& name, const string& iata, const string& city, const string& country, double la, double lo):
        cost(INT_MAX),
        airport_id(id),
        airport_name(name),
        airport_iata(iata),
        airport_city(city),
        airport_country(country),
        lat(la),
        longi(lo),
        prev(NULL)
    {}
    void pushNewRoute(Routes* route)
    {
        routes.push_back(route);
    }
    friend ostream& operator<<(ostream& out, const Vertex& other)
    {
        out << "Airport: " << other.airport_name << endl;
        for(vector<Routes*>::const_iterator it = other.routes.begin(); it != other.routes.end(); ++it)
        {
            out << "Distance: " << (*it)->distance << " nextAirport: " << (*it)->nextAirport << " Airlines: \n";
            for(vector<string>::iterator mt = (*it)->carriers.begin(); mt != (*it)->carriers.end(); ++mt)
            {
                out << *mt << " ";
            }
        }
        out << endl;
        return out;
    }
    vector<Routes*> routes;
    double cost;
    int airport_id;
    string airport_name, airport_iata, airport_icao, airport_city, airport_country;
    double lat, longi;
    Vertex* prev;
};

struct sort_pred
{
    bool operator()(Routes* x, Routes* y)
    {
        return x->distance < y->distance;
    }
};

class Prioritize
{
    public:
        bool operator()(Vertex* p1 ,Vertex* p2)
        {
            return p1->cost <= p2->cost;
        }
};

#endif // CONTAINERS
