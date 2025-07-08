# Musicopedia Backend

## Data Models
<details>
<summary>Show/Hide Data Models</summary>

### Artist
- `artistId` (UUID): Unique identifier
- `spotifyId` (String): Spotify ID
- `artistName` (String): Name of the artist
- `description` (String): Description
- `image` (String): Image URL
- `type` (ArtistType): Type (e.g., group, solo)
- `primaryLanguage` (String): Main language
- `genre` (String): Genre
- `originCountry` (String): Country code

### Groups
- `artistId` (UUID): Unique identifier (linked to Artist)
- `artist` (Artist): Associated artist entity
- `formationDate` (LocalDate): Formation date
- `disbandDate` (LocalDate): Disband date
- `groupGender` (ArtistGender): Gender
- `activityStatus` (GroupActivityStatus): Status (active/disbanded)

### Solo
- `artistId` (UUID): Unique identifier (linked to Artist)
- `artist` (Artist): Associated artist entity
- `birthDate` (LocalDate): Birth date
- `deathDate` (LocalDate): Death date
- `debutDate` (LocalDate): Debut date
- `gender` (ArtistGender): Gender
- `groupAffiliationStatus` (GroupAffiliationStatus): Group affiliation
- `realName` (String): Real name
- `member` (Member): Linked member

### Subunit
- `subunitId` (UUID): Unique identifier
- `mainGroup` (Groups): Parent group
- `subunitName` (String): Name
- `description` (String): Description
- `image` (String): Image URL
- `formationDate` (LocalDate): Formation date
- `disbandDate` (LocalDate): Disband date
- `subunitGender` (ArtistGender): Gender
- `activityStatus` (GroupActivityStatus): Status
- `originCountry` (String): Country code
- `groupSubunit` (Groups): Linked group entity (if debuted)

### Member
- `memberId` (UUID): Unique identifier
- `memberName` (String): Stage name
- `realName` (String): Real name
- `description` (String): Description
- `image` (String): Image URL
- `birthDate` (LocalDate): Birth date
- `deathDate` (LocalDate): Death date (if deceased)
- `soloIdentities` (List<Solo>): Solo identities
- `nationality` (String): Country code

### GroupMembership
- `id` (GroupMembershipId): Composite key
- `group` (Artist): Group
- `member` (Member): Member
- `status` (MembershipStatus): Status (active/former)
- `joinDate` (LocalDate): Join date
- `leaveDate` (LocalDate): Leave date

### SubunitMembership
- `id` (SubunitMembershipId): Composite key
- `subunit` (Subunit): Subunit
- `member` (Member): Member
- `joinedDate` (LocalDate): Join date
- `leftDate` (LocalDate): Leave date

</details>

## Entity Relationships
<details>
<summary>Show/Hide Entity Relationships</summary>

### Artist
- One-to-one with `Groups`: Each `Artist` can be a group, represented by a single `Groups` entity sharing the same ID. This means a group artist has both an `Artist` and a `Groups` record, linked by `artistId`.
- One-to-one with `Solo`: Each `Artist` can also be a soloist, represented by a single `Solo` entity sharing the same ID. A solo artist has both an `Artist` and a `Solo` record, linked by `artistId`.

### Groups
- One-to-one with `Artist`: Every `Groups` entity is directly linked to an `Artist` entity (type = group) via the same `artistId`.
- One-to-many with `Subunit`: A group can have multiple subunits. This is modeled by the `mainGroup` field in `Subunit`, which references the parent `Groups` entity.

### Solo
- One-to-one with `Artist`: Every `Solo` entity is directly linked to an `Artist` entity (type = solo) via the same `artistId`.
- Many-to-one with `Member`: Each `Solo` entity references a `Member` (the person behind the solo identity). A member can have multiple solo identities, but each solo identity belongs to only one member.

### Subunit
- Many-to-one with `Groups`: Each subunit belongs to a main group, referenced by the `mainGroup` field.
- One-to-one with `Groups`: If a subunit has debuted as a group, it is also linked to a `Groups` entity via the `groupSubunit` field.
- Many-to-many with `Member` via `SubunitMembership`: Members can belong to multiple subunits, and subunits can have multiple members. This relationship is managed by the `SubunitMembership` join table.

### Member
- One-to-many with `Solo`: A member can have multiple solo identities (e.g., different stage names or solo projects), but each solo identity is linked to only one member.
- Many-to-many with `Groups` via `GroupMembership`: Members can belong to multiple groups, and groups can have multiple members. This is managed by the `GroupMembership` join table.
- Many-to-many with `Subunit` via `SubunitMembership`: Members can belong to multiple subunits, and subunits can have multiple members. This is managed by the `SubunitMembership` join table.

### GroupMembership
- Many-to-one with `Artist` (group): Each `GroupMembership` entry links a member to a group (artist of type group).
- Many-to-one with `Member`: Each `GroupMembership` entry links a group to a member. The combination of group and member forms the composite key.
- Contains additional fields like `status`, `joinDate`, and `leaveDate` to track the membership period and status (active or former).

### SubunitMembership
- Many-to-one with `Subunit`: Each `SubunitMembership` entry links a member to a subunit.
- Many-to-one with `Member`: Each `SubunitMembership` entry links a subunit to a member. The combination of subunit and member forms the composite key.
- Contains additional fields like `joinedDate` and `leftDate` to track the membership period in the subunit.

</details>

## API Controllers & Commands
<details>
<summary>Show/Hide API Controllers & Commands</summary>

### ArtistController (`/api/artists`)
- `GET /api/artists` — Get all artists
- `GET /api/artists/{id}` — Get artist by ID
- `GET /api/artists/search?name=...` — Search artists by name
- `GET /api/artists/spotify/{spotifyId}` — Get artist by Spotify ID
- `GET /api/artists/type/{type}` — Get artists by type (e.g., group, solo)
- `POST /api/artists` — Create a new artist

### GroupController (`/api/groups`)
- `GET /api/groups` — Get all groups
- `GET /api/groups/{id}` — Get group by ID
- `GET /api/groups/formation-date?start=...&end=...` — Get groups by formation date range
- `GET /api/groups/active` — Get active groups
- `GET /api/groups/disbanded` — Get disbanded groups
- `GET /api/groups/gender/{gender}` — Get groups by gender

### MemberController (`/api/members`)
- `GET /api/members` — Get all members
- `GET /api/members/{id}` — Get member by ID
- `GET /api/members/search?name=...` — Search members by name
- `GET /api/members/birthdate?start=...&end=...` — Get members by birth date range
- `POST /api/members` — Create a new member

### SoloController (`/api/soloists`)
- `GET /api/soloists` — Get all soloists
- `GET /api/soloists/{id}` — Get soloist by ID
- `GET /api/soloists/birthdate?start=...&end=...` — Get soloists by birth date range
- `GET /api/soloists/gender/{gender}` — Get soloists by gender
- `GET /api/soloists/active` — Get active soloists
- `GET /api/soloists/deceased` — Get deceased soloists

### SubunitController (`/api/subunits`)
- `GET /api/subunits` — Get all subunits
- `GET /api/subunits/{id}` — Get subunit by ID
- `POST /api/subunits` — Create a new subunit
- `PUT /api/subunits/{id}` — Update a subunit
- `DELETE /api/subunits/{id}` — Delete a subunit

### GroupMembershipController (`/api/memberships`)
- `GET /api/memberships/group/{groupId}` — Get memberships by group ID
- `GET /api/memberships/member/{memberId}` — Get memberships by member ID
- `GET /api/memberships/group/{groupId}/status/{status}` — Get memberships by group and status
- `GET /api/memberships/group/{groupId}/former-members` — Get former members by group ID
- `GET /api/memberships/group/{groupId}/joined-after?date=...` — Get members who joined after a date
- `GET /api/memberships/group/{groupId}/left-before?date=...` — Get members who left before a date

### SubunitMembershipController (`/api/subunit-memberships`)
- `GET /api/subunit-memberships/subunit/{subunitId}` — Get memberships by subunit
- `GET /api/subunit-memberships/member/{memberId}` — Get memberships by member
- `DELETE /api/subunit-memberships/subunit/{subunitId}` — Delete all memberships by subunit
- `DELETE /api/subunit-memberships/member/{memberId}` — Delete all memberships by member
- `GET /api/subunit-memberships/exists?subunitId=...&memberId=...` — Check if a membership exists

</details>