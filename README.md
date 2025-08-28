# HealSync 🏥

A modern **Patient Management System** built for scalability and responsiveness using Spring Boot microservices, GraphQL APIs for flexible data querying, and real-time WebSocket updates, all containerized with Docker.

## Architecture Overview


##  Features

- **Microservices Architecture**: Each service built with Spring Boot, focused on single responsibilities for modularity, scalability, and easier maintenance
- **GraphQL API**: Offers flexible data fetching with queries, mutations, and real-time **subscriptions** via WebSocket
- **Real-Time Communication**: WebSocket integration for live updates (appointment changes, notifications) to clients
- **Dockerized Deployment**: Each microservice runs in its own container, simplifying development, testing, and deployment workflows
- **Service Discovery**: Spring Cloud for dynamic service registration and discovery
- **API Gateway**: Centralized routing, authentication, and load balancing

## Technology Stack

| Function | Tech Stack / Tools |
|----------|-------------------|
| **Backend Services** | Spring Boot (Java microservices) |
| **API Layer** | GraphQL (Spring GraphQL) with subscription support |
| **Real-time Updates** | WebSocket (Spring GraphQL WebSocket integration) |
| **Containerization** | Docker images & containers across services |
| **Service Discovery** | Spring Cloud (Config, Discovery) |
| **Security** | Spring Security for authentication & authorization |
| **Database** | PostgreSQL, MongoDB (per service requirements) |
| **Message Queue** | Apache Kafka for inter-service communication |

## Microservices Architecture

```
+----------------------+     +---------------------+
|   API Gateway / UI   |<--->| GraphQL Endpoint(s) |
+----------------------+     +---------------------+
           |                          |
           v                          v
+----------------+         +----------------+
| Patient Service|         | Appointment Svc|
+----------------+         +----------------+
           |                          |
      +----------------+    +----------------+
      | Doctor Service |    | Billing Service|
      +----------------+    +----------------+
           |
+--------------------+
| Notification via WS|
+--------------------+
```

### Service Boundaries

- **Patient Service**: Patient registration, profile management, medical history
- **Appointment Service**: Scheduling, availability, appointment lifecycle
- **Doctor Service**: Doctor profiles, specializations, schedules
- **Notification Service**: Real-time alerts, email/SMS notifications
- **Billing Service**: Payment processing, insurance claims, invoicing
- **Medical Records Service**: Lab results, prescriptions, treatment history

## GraphQL Schema Structure

```graphql
type Patient {
  id: ID!
  name: String!
  email: String!
  phone: String
  dateOfBirth: String
  medicalHistory: [MedicalRecord!]!
  appointments: [Appointment!]!
}

type Appointment {
  id: ID!
  patient: Patient!
  doctor: Doctor!
  scheduledAt: String!
  status: AppointmentStatus!
  notes: String
}

type Doctor {
  id: ID!
  name: String!
  specialization: String!
  availableSlots: [TimeSlot!]!
  appointments: [Appointment!]!
}

type Query {
  patients(limit: Int, offset: Int): [Patient!]!
  patient(id: ID!): Patient
  appointments(date: String, doctorId: ID): [Appointment!]!
  doctors(specialization: String): [Doctor!]!
}

type Mutation {
  createPatient(input: PatientInput!): Patient!
  bookAppointment(input: AppointmentInput!): Appointment!
  updateAppointmentStatus(id: ID!, status: AppointmentStatus!): Appointment!
}

type Subscription {
  appointmentUpdated(patientId: ID): Appointment!
  newNotification(userId: ID!): Notification!
  patientUpdated(id: ID!): Patient!
}
```

## Getting Started

### Prerequisites
- Java 17+
- Docker & Docker Compose
- Maven 3.6+

### Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/tusquake/HealSync.git
   cd HealSync
   ```

2. **Start all services with Docker Compose**
   ```bash
   docker-compose up --build
   ```

3. **Access GraphQL Playground**
   ```
   http://localhost:8080/graphiql
   ```

4. **WebSocket endpoint for subscriptions**
   ```
   ws://localhost:8080/graphql
   ```

### Individual Service Development

```bash
# Start specific service
cd patient-service
mvn spring-boot:run

# Build Docker image
docker build -t healsync/patient-service .
```

## Project Structure

```
HealSync/
├── api-gateway/                 # Spring Cloud Gateway
│   ├── src/main/java/
│   └── Dockerfile
├── patient-service/            # Patient management microservice
│   ├── src/main/java/
│   ├── src/main/resources/
│   └── Dockerfile
├── appointment-service/        # Appointment scheduling
├── doctor-service/            # Doctor profile management
├── notification-service/      # Real-time notifications
├── billing-service/           # Payment & billing
├── medical-records-service/   # Medical history
├── shared-models/             # Common DTOs and entities
├── docker-compose.yml         # Multi-service orchestration
├── k8s/                      # Kubernetes deployment configs
└── docs/                     # Architecture diagrams
```

## Development Workflow

### Local Development
```bash
# Start infrastructure services
docker-compose -f docker-compose.dev.yml up -d

# Run services individually
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Testing
```bash
# Unit tests
mvn test

# Integration tests
mvn verify -Dtest.profile=integration

# GraphQL API tests
curl -X POST http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"{ patients { id name email } }"}'
```

## Security & Authentication

- **JWT-based authentication** via Spring Security
- **Role-based authorization** (PATIENT, DOCTOR, ADMIN)
- **GraphQL field-level security** with @PreAuthorize
- **HTTPS/WSS** for secure communication

## Real-Time Features

### WebSocket Subscriptions
```javascript
// Subscribe to appointment updates
subscription {
  appointmentUpdated(patientId: "123") {
    id
    status
    scheduledAt
    doctor {
      name
    }
  }
}

// Subscribe to notifications
subscription {
  newNotification(userId: "456") {
    id
    message
    type
    timestamp
  }
}
```

## Docker Configuration

### Service Composition
```yaml
services:
  api-gateway:
    build: ./api-gateway
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
  
  patient-service:
    build: ./patient-service
    environment:
      - DATABASE_URL=postgres://db:5432/patients
    depends_on:
      - postgres
      
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: healsync
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: secret
```


## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/docter-service`)
3. Follow the coding standards and add tests
4. Commit your changes (`git commit -m 'Add Doctor Service'`)
5. Push to the branch (`git push origin feature/docter-service`)
6. Open a Pull Request

### Development Guidelines
- Follow Spring Boot best practices
- Write comprehensive tests (unit + integration)
- Document GraphQL schema changes
- Update Docker configurations as needed
- Maintain service independence

## Performance & Scaling

- **Horizontal scaling** via Docker Swarm/Kubernetes
- **Database per service** pattern for data isolation
- **Load balancing** at API Gateway level
- **Async processing** with message queues

## Support

- [Linkedin](https://www.linkedin.com/in/sethtushar111/)
- [Twitter](https://x.com/TUSHARS96468835)


---

**HealSync** - Modern Healthcare Management through Microservices Architecture
